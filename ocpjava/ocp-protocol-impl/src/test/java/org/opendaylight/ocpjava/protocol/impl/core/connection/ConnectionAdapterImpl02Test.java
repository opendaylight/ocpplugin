/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2016 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionAdapterImpl;
import org.opendaylight.ocpjava.protocol.impl.core.connection.MessageListenerWrapper;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ResponseExpectedRpcListener;
import org.opendaylight.ocpjava.protocol.impl.core.connection.RpcResponseKey;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateInput;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @author madamjak
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 * 
 */
public class ConnectionAdapterImpl02Test {
    private static final int RPC_RESPONSE_EXPIRATION = 1;
    private static final RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>> REMOVAL_LISTENER =
            new RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>>() {
        @Override
        public void onRemoval(
                final RemovalNotification<RpcResponseKey, ResponseExpectedRpcListener<?>> notification) {
            notification.getValue().discard();
        }
    };

    @Mock HealthCheckInput healthCheckInput;
    @Mock GetParamInput getParamInput;
    @Mock GetStateInput getStateInput;
    
    private ConnectionAdapterImpl adapter;
    private Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> cache;
    private OcpHeader responseOfCall;
    
    /**
     * Initialize mocks
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    /**
     * Disconnect adapter
     */
    @After
    public void tierDown(){
        if (adapter != null && adapter.isAlive()) {
            adapter.disconnect();
        }
    }
    
    /**
     * Test Rpc Calls
     */
    @Test
    public void testRcp() {
        EmbeddedChannel embChannel = new EmbeddedChannel(new EmbededChannelHandler());
        adapter = new ConnectionAdapterImpl(embChannel,InetSocketAddress.createUnresolved("localhost", 9876));
        cache = CacheBuilder.newBuilder().concurrencyLevel(1).expireAfterWrite(RPC_RESPONSE_EXPIRATION, TimeUnit.MINUTES)
                .removalListener(REMOVAL_LISTENER).build();
        adapter.setResponseCache(cache);

        // -- healthCheck
        adapter.healthCheck(healthCheckInput);
        embChannel.runPendingTasks();
        Assert.assertEquals("Wrong - healthCheck", healthCheckInput, responseOfCall);

        // -- getParam
        adapter.getParam(getParamInput);
        embChannel.runPendingTasks();
        Assert.assertEquals("Wrong - getParam", getParamInput, responseOfCall);

        // -- getState
        adapter.getState(getStateInput);
        embChannel.runPendingTasks();
        Assert.assertEquals("Wrong - getState", getStateInput, responseOfCall);
        
        
        adapter.disconnect();
    }

    /**
     * Channel Handler for testing
     * @author madamjak
     *
     */
    private class EmbededChannelHandler extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg,
                ChannelPromise promise) throws Exception {
            responseOfCall = null;
            if(msg instanceof MessageListenerWrapper){
                MessageListenerWrapper listener = (MessageListenerWrapper) msg;
                OcpHeader ocpHeader = listener.getMsg();
                responseOfCall = ocpHeader;
            }
        }
    }
}
