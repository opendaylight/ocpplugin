/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */


package org.opendaylight.ocpjava.protocol.impl.clients;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

import com.google.common.util.concurrent.SettableFuture;

/** Initializes secured {@link SimpleClient} pipeline
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class SimpleClientInitializer extends ChannelInitializer<NioSocketChannel> {

    private SettableFuture<Boolean> isOnlineFuture;
    private boolean secured;
    private ScenarioHandler scenarioHandler;

    /**
     * @param isOnlineFuture future notifier of connected channel
     * @param secured true if {@link SimpleClient} should use encrypted communication
     */
    public SimpleClientInitializer(SettableFuture<Boolean> isOnlineFuture, boolean secured) {
        this.isOnlineFuture = isOnlineFuture;
        this.secured = secured;
    }

    @Override
    public void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (secured) {
            SSLEngine engine = ClientSslContextFactory.getClientContext()
                    .createSSLEngine();
            engine.setUseClientMode(true);
            pipeline.addLast("ssl", new SslHandler(engine));
        }
        SimpleClientHandler simpleClientHandler = new SimpleClientHandler(isOnlineFuture, scenarioHandler);
        simpleClientHandler.setScenario(scenarioHandler);
        pipeline.addLast("framer", new SimpleClientFramer());
        pipeline.addLast("handler", simpleClientHandler);
        isOnlineFuture = null;
    }

    /**
     * @param scenarioHandler handler of scenario events
     */
    public void setScenario(ScenarioHandler scenarioHandler) {
        this.scenarioHandler = scenarioHandler;
    }
}