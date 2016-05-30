/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2016 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import static org.mockito.Mockito.when;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionReadyListener;
import org.opendaylight.ocpjava.statistics.CounterEventTypes;
import org.opendaylight.ocpjava.statistics.OcpStatisticsCounters;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessageBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.OcpProtocolListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.SystemNotificationsListener;
import org.opendaylight.yangtools.yang.binding.DataObject;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * Test counters in ConnectionAdapter (at least DS_ENTERED_OCPJAVA, DS_FLOW_MODS_ENTERED and US_MESSAGE_PASS counters have to be enabled)
 * @author madamjak
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class ConnectionAdapterImplOcpStatisticsTest {

    private static final int RPC_RESPONSE_EXPIRATION = 1;
    private static final RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>> REMOVAL_LISTENER =
            new RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>>() {
        @Override
        public void onRemoval(
                final RemovalNotification<RpcResponseKey, ResponseExpectedRpcListener<?>> notification) {
            notification.getValue().discard();
        }
    };

    @Mock SystemNotificationsListener systemListener;
    @Mock ConnectionReadyListener readyListener;
    @Mock ChannelFuture channelFuture;
    @Mock OcpProtocolListener messageListener;
    @Mock OcpExtensionListener messageExtListener;
    @Mock SocketChannel channel;
    @Mock ChannelPipeline pipeline;
    @Mock HealthCheckInput healthCheckInput;
    @Mock GetParamInput getParamInput;
    
    private ConnectionAdapterImpl adapter;
    private Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> cache;
    private OcpStatisticsCounters statCounters;

    /**
     * Initialize mocks
     * Start counting and reset counters before each test
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        statCounters = OcpStatisticsCounters.getInstance();
        statCounters.startCounting(false, 0);
    }

    /**
     * Disconnect adapter
     * Stop counting after each test
     */
    @After
    public void tierDown(){
        if (adapter != null && adapter.isAlive()) {
            adapter.disconnect();
        }
        statCounters.stopCounting();
    }

    /**
     * Test statistic counter for all rpc calls (counters DS_ENTERED_OCPJAVA and DS_FLOW_MODS_ENTERED have to be enabled)
     */
    @Test
    public void testEnterOFJavaCounter() {
        if(!statCounters.isCounterEnabled(CounterEventTypes.DS_ENTERED_OCPJAVA)){
            Assert.fail("Counter " + CounterEventTypes.DS_ENTERED_OCPJAVA + " is not enabled");
        }
        if(!statCounters.isCounterEnabled(CounterEventTypes.DS_FLOW_MODS_ENTERED)){
            Assert.fail("Counter " + CounterEventTypes.DS_FLOW_MODS_ENTERED + " is not enabled");
        }
        EmbeddedChannel embChannel = new EmbeddedChannel(new EmbededChannelHandler());
        adapter = new ConnectionAdapterImpl(embChannel,InetSocketAddress.createUnresolved("localhost", 9876));
        cache = CacheBuilder.newBuilder().concurrencyLevel(1).expireAfterWrite(RPC_RESPONSE_EXPIRATION, TimeUnit.MINUTES)
                .removalListener(REMOVAL_LISTENER).build();
        adapter.setResponseCache(cache);
        adapter.healthCheck(healthCheckInput);
        adapter.getParam(getParamInput);

        embChannel.runPendingTasks();
        Assert.assertEquals("Wrong - bad counter value for ConnectionAdapterImpl rpc methods", 2, statCounters.getCounter(CounterEventTypes.DS_ENTERED_OCPJAVA).getCounterValue());
        adapter.disconnect();
    }

    /**
     * Test counter for pass messages to consumer (counter US_MESSAGE_PASS has to be enabled)
     */
    @Test
    public void testMessagePassCounter() {
        if(!statCounters.isCounterEnabled(CounterEventTypes.US_MESSAGE_PASS)){
            Assert.fail("Counter " + CounterEventTypes.US_MESSAGE_PASS + " is not enabled");
        }
        when(channel.pipeline()).thenReturn(pipeline);
        adapter = new ConnectionAdapterImpl(channel, InetSocketAddress.createUnresolved("10.0.0.1", 6653));
        adapter.setMessageListener(messageListener);
        adapter.setMessageExtListener(messageExtListener);
        adapter.setSystemListener(systemListener);
        adapter.setConnectionReadyListener(readyListener);
        cache = CacheBuilder.newBuilder().concurrencyLevel(1).expireAfterWrite(RPC_RESPONSE_EXPIRATION, TimeUnit.MINUTES)
                .removalListener(REMOVAL_LISTENER).build();
        adapter.setResponseCache(cache);
        when(channel.disconnect()).thenReturn(channelFuture);
        DataObject message = new HelloMessageBuilder().build();
        adapter.consume(message);
        Assert.assertEquals("Wrong - bad counter value for ConnectionAdapterImpl consume method", 1, statCounters.getCounter(CounterEventTypes.US_MESSAGE_PASS).getCounterValue());
        adapter.disconnect();
    }

    /**
     * Empty channel Handler for testing
     * @author madamjak
     *
     */
    private class EmbededChannelHandler extends ChannelOutboundHandlerAdapter {
        // no operation need to test
    }
}
