/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */


package org.opendaylight.ocpjava.protocol.impl.clients;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

import org.opendaylight.ocpjava.util.ByteBufUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.SettableFuture;

/**
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleClientHandler.class);
    private SettableFuture<Boolean> isOnlineFuture;
    protected ScenarioHandler scenarioHandler;

    /**
     * @param isOnlineFuture future notifier of connected channel
     * @param scenarioHandler handler of scenario events
     */
    public SimpleClientHandler(SettableFuture<Boolean> isOnlineFuture, ScenarioHandler scenarioHandler) {
        this.isOnlineFuture = isOnlineFuture;
        this.scenarioHandler = scenarioHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("channel Read message: " + msg);

        byte[] bs = (byte[])msg;
        scenarioHandler.addOcpMsg(bs);

        String buf = new String(bs, "UTF-8");
        LOGGER.debug("<< {}", buf);
        LOGGER.debug("end of read");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("Client1 is active");
        if (isOnlineFuture != null) {
            isOnlineFuture.set(true);
            isOnlineFuture = null;
        }
        scenarioHandler.setCtx(ctx);
        scenarioHandler.start();
    }

    /**
     * @param scenarioHandler handler of scenario events
     */
    public void setScenario(ScenarioHandler scenarioHandler) {
        this.scenarioHandler = scenarioHandler;
    }
}
