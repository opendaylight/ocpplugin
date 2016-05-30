/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2016 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionReadyListener;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionAdapterImpl;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ResponseExpectedRpcListener;
import org.opendaylight.ocpjava.protocol.impl.core.connection.RpcResponseKey;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessageBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutputBuilder;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.OcpProtocolListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.DisconnectEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.DisconnectEventBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.RadioHeadIdleEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.RadioHeadIdleEventBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.SystemNotificationsListener;
import org.opendaylight.yangtools.yang.binding.DataObject;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @author michal.polkorab
 * @author madamjak
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class ConnectionAdapterImplTest {

    private static final int RPC_RESPONSE_EXPIRATION = 1;
    private static final RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>> REMOVAL_LISTENER =
            new RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>>() {
        @Override
        public void onRemoval(
                final RemovalNotification<RpcResponseKey, ResponseExpectedRpcListener<?>> notification) {
            notification.getValue().discard();
        }
    };

    @Mock SocketChannel channel;
    @Mock ChannelPipeline pipeline;
    @Mock OcpProtocolListener messageListener;
    @Mock OcpExtensionListener messageExtListener;
    @Mock SystemNotificationsListener systemListener;
    @Mock ConnectionReadyListener readyListener;
    @Mock Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> mockCache;
    @Mock ChannelFuture channelFuture;

    private ConnectionAdapterImpl adapter;
    private Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> cache;

    /**
     * Initializes ConnectionAdapter
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
    }

    /**
     * Tests {@link ConnectionAdapterImpl#consume(DataObject)} with notifications
     */
    @Test
    public void testConsume() {
        DataObject message = new HelloMessageBuilder().build();
        adapter.consume(message);
        verify(messageExtListener, times(1)).onHelloMessage((HelloMessage) message);
        message = new RadioHeadIdleEventBuilder().build();
        adapter.consume(message);
        verify(systemListener, times(1)).onRadioHeadIdleEvent((RadioHeadIdleEvent) message);
        message = new DisconnectEventBuilder().build();
        adapter.consume(message);
        verify(systemListener, times(1)).onDisconnectEvent((DisconnectEvent) message);
    }

    /**
     * Tests {@link ConnectionAdapterImpl#consume(DataObject)} with unexpected rpc
     */
    @Test
    public void testConsume2() {
        adapter.setResponseCache(mockCache);
        HealthCheckOutputBuilder healthCheckBuilder = new HealthCheckOutputBuilder();
        healthCheckBuilder.setXid(42L);
        HealthCheckOutput healthCheck = healthCheckBuilder.build();
        adapter.consume(healthCheck);
        verify(mockCache, times(1)).getIfPresent(any(RpcResponseKey.class));
    }

    /**
     * Tests {@link ConnectionAdapterImpl#consume(DataObject)} with expected rpc
     */
    @Test
    public void testConsume3() {
        GetParamInputBuilder inputBuilder = new GetParamInputBuilder();
        inputBuilder.setXid(2L);
        GetParamInput getParamInput = inputBuilder.build();
        RpcResponseKey key = new RpcResponseKey(2L, "org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput");
        ResponseExpectedRpcListener<OcpHeader> listener = new ResponseExpectedRpcListener<>(getParamInput,
                "failure", mockCache, key);
        cache.put(key, listener);

        ResponseExpectedRpcListener<?> ifPresent2 = cache.getIfPresent(key);
        Assert.assertNotNull("Listener not in cache", ifPresent2);
        
        GetParamOutputBuilder getParamBuilder = new GetParamOutputBuilder();
        getParamBuilder.setXid(2L);
        GetParamOutput getParamOutput = getParamBuilder.build();
        adapter.consume(getParamOutput);
        ResponseExpectedRpcListener<?> ifPresent = cache.getIfPresent(key);
        Assert.assertNull("Listener was not discarded", ifPresent);
    }

    /**
     * Test IsAlive method
     */
    @Test
    public void testIsAlive(){
        int port = 9876;
        String host ="localhost";
        InetSocketAddress inetSockAddr = InetSocketAddress.createUnresolved(host, port);
        ConnectionAdapterImpl connAddapter = new ConnectionAdapterImpl(channel,inetSockAddr);
        Assert.assertEquals("Wrong - diffrence between channel.isOpen() and ConnectionAdapterImpl.isAlive()", channel.isOpen(), connAddapter.isAlive());

        connAddapter.disconnect();
        Assert.assertFalse("Wrong - ConnectionAdapterImpl can not be alive after disconnet.", connAddapter.isAlive());
    }

    /**
     * Test throw exception if no listeners are present
     */
    @Test(expected = java.lang.IllegalStateException.class)
    public void testMissingListeners(){
        int port = 9876;
        String host ="localhost";
        InetSocketAddress inetSockAddr = InetSocketAddress.createUnresolved(host, port);
        ConnectionAdapterImpl connAddapter = new ConnectionAdapterImpl(channel,inetSockAddr);
        connAddapter.setSystemListener(null);
        connAddapter.setMessageListener(null);
        connAddapter.setMessageExtListener(null);
        connAddapter.setConnectionReadyListener(null);
        connAddapter.checkListeners();
    }
}
