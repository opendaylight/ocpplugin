/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2016 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import io.netty.channel.ChannelHandler;
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
import org.opendaylight.ocpjava.protocol.impl.core.connection.ChannelOutboundQueue;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionAdapterImpl;
import org.opendaylight.ocpjava.protocol.impl.core.connection.MessageListenerWrapper;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ResponseExpectedRpcListener;
import org.opendaylight.ocpjava.protocol.impl.core.connection.RpcResponseKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @author madamjak
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class ChannelOutboundQueue02Test {
    private static int counter;
    private static final int RPC_RESPONSE_EXPIRATION = 1;
    private static final RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>> REMOVAL_LISTENER =
            new RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>>() {
        @Override
        public void onRemoval(
                final RemovalNotification<RpcResponseKey, ResponseExpectedRpcListener<?>> notification) {
            notification.getValue().discard();
        }
    };

    @Mock HelloInput helloInput;
    @Mock HealthCheckInput healthCheckInput;
    @Mock GetParamInput getParamInput;
    
    
    private ConnectionAdapterImpl adapter;
    private Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> cache;
    /**
     * Initialize mocks
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    /**
     * Disconnect adapter after each test
     */
    @After
    public void tierDown(){
        if (adapter != null && adapter.isAlive()) {
            adapter.disconnect();
        }
    }

    /**
     * Test write to closed / opened channel
     * @throws Exception
     */
    @Test
    public void test01() throws Exception {
        EmbeddedChannel ec = new EmbeddedChannel(new EmbededChannelHandler());
        adapter = new ConnectionAdapterImpl(ec,InetSocketAddress.createUnresolved("localhost", 9876));
        cache = CacheBuilder.newBuilder().concurrencyLevel(1).expireAfterWrite(RPC_RESPONSE_EXPIRATION, TimeUnit.MINUTES)
                .removalListener(REMOVAL_LISTENER).build();
        adapter.setResponseCache(cache);
        ChannelOutboundQueue cq = (ChannelOutboundQueue) ec.pipeline().last();
        counter=0;
        adapter.healthCheck(healthCheckInput);
        adapter.hello(helloInput);
        cq.channelInactive(ec.pipeline().lastContext());
        ec.runPendingTasks();
        Assert.assertEquals("Wrong - ChannelOutboundHandlerAdapter.write was invoked on closed channel",0, counter);
        cq.channelActive(ec.pipeline().lastContext());
        counter=0;
        adapter.healthCheck(healthCheckInput);
        adapter.getParam(getParamInput);
        ec.runPendingTasks();
        Assert.assertEquals("Wrong - ChannelOutboundHandlerAdapter.write has not been invoked on opened channel",2, counter);
    }


    /**
     * Test write to read only / writable channel
     */
    @Test
    public void test02(){
        ChangeWritableEmbededChannel ec = new ChangeWritableEmbededChannel(new EmbededChannelHandler());
        adapter = new ConnectionAdapterImpl(ec,InetSocketAddress.createUnresolved("localhost", 9876));
        cache = CacheBuilder.newBuilder().concurrencyLevel(1).expireAfterWrite(RPC_RESPONSE_EXPIRATION, TimeUnit.MINUTES)
                .removalListener(REMOVAL_LISTENER).build();
        adapter.setResponseCache(cache);
        ec.setReadOnly();
        counter=0;
        adapter.healthCheck(healthCheckInput);
        adapter.hello(helloInput);
        ec.runPendingTasks();
        Assert.assertEquals("Wrong - write to readonly channel",0, counter);
        ec.setWritable();
        adapter.healthCheck(healthCheckInput);
        adapter.getParam(getParamInput);
        ec.runPendingTasks();
        Assert.assertEquals("Wrong - write to writtable channel",4, counter);
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
            if(msg instanceof MessageListenerWrapper){
                counter++;
            }
        }
    }

    /**
     * Class for testing - channel can change state to read only or writable
     * @author madamjak
     *
     */
    private class ChangeWritableEmbededChannel extends EmbeddedChannel {
        private boolean isWrittable;
        public ChangeWritableEmbededChannel(ChannelHandler channelHandler){
            super(channelHandler);
            setReadOnly();
        }

        @Override
        public boolean isWritable() {
            return isWrittable;
        }

        public void setWritable(){
            isWrittable = true;
        }

        public void setReadOnly(){
            isWrittable = false;
        }
    }
}
