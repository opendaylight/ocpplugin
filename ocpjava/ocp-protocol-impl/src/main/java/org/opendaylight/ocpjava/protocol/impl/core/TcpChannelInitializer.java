/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngine;

import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionAdapterFactory;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionAdapterFactoryImpl;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes TCP / TLS channel
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class TcpChannelInitializer extends ProtocolChannelInitializer<SocketChannel> {

    private static final Logger LOG = LoggerFactory
            .getLogger(TcpChannelInitializer.class);
    private final DefaultChannelGroup allChannels;
    private ConnectionAdapterFactory connectionAdapterFactory;

    /**
     * default ctor
     */
    public TcpChannelInitializer() {
        this( new DefaultChannelGroup("netty-receiver", null), new ConnectionAdapterFactoryImpl() );
    }

    /**
     * Testing Constructor
     *
     */
    protected TcpChannelInitializer( DefaultChannelGroup channelGroup, ConnectionAdapterFactory connAdaptorFactory ) {
    	allChannels = channelGroup ;
    	connectionAdapterFactory = connAdaptorFactory ;
    }

    @Override
    protected void initChannel(final SocketChannel ch) {
        if (ch.remoteAddress() != null) {
            InetAddress radioHeadAddress = ch.remoteAddress().getAddress();
            int port = ch.localAddress().getPort();
            int remotePort = ch.remoteAddress().getPort();
            LOG.debug("Incoming connection from (remote address): {}:{} --> :{}",
			    radioHeadAddress.toString(), remotePort, port);

            if (!getRadioHeadConnectionHandler().accept(radioHeadAddress)) {
                ch.disconnect();
                LOG.debug("Incoming connection rejected");
                return;
            }
        }
        LOG.debug("Incoming connection accepted - building pipeline");
        allChannels.add(ch);
        ConnectionFacade connectionFacade = null;
        connectionFacade = connectionAdapterFactory.createConnectionFacade(ch, null);
        try {
            LOG.trace("initChannel - getRadioHeadConnectionHandler: {}", getRadioHeadConnectionHandler());
            getRadioHeadConnectionHandler().onRadioHeadConnected(connectionFacade);
            connectionFacade.checkListeners();
            ch.pipeline().addLast(PipelineHandlers.IDLE_HANDLER.name(), new IdleHandler(getRadioHeadIdleTimeout(), TimeUnit.MILLISECONDS));
            boolean tlsPresent = false;

            // If this channel is configured to support SSL it will only support SSL
            if (getTlsConfiguration() != null) {
                tlsPresent = true;
                SslContextFactory sslFactory = new SslContextFactory(getTlsConfiguration());
                SSLEngine engine = sslFactory.getServerContext().createSSLEngine();
                engine.setNeedClientAuth(true);
                engine.setUseClientMode(false);
                ch.pipeline().addLast(PipelineHandlers.SSL_HANDLER.name(), new SslHandler(engine));
            }

            ch.pipeline().addLast(PipelineHandlers.OCP_XML_DECODER.name(), new OCPXmlDecoder(connectionFacade, tlsPresent));
            LOG.trace("TcpChannelInitializer - initChannel OCPXmlDecoder()");

            OCPDecoder ocpDecoder = new OCPDecoder();
            ocpDecoder.setDeserializationFactory(getDeserializationFactory());
            ch.pipeline().addLast(PipelineHandlers.OCP_DECODER.name(), ocpDecoder);
            LOG.trace("TcpChannelInitializer - initChannel ocpDecoder()");

            OCPEncoder ocpEncoder = new OCPEncoder();
            ocpEncoder.setSerializationFactory(getSerializationFactory());
            ch.pipeline().addLast(PipelineHandlers.OCP_ENCODER.name(), ocpEncoder);
            LOG.trace("TcpChannelInitializer - initChannel ocpEncoder()");

            ch.pipeline().addLast(PipelineHandlers.DELEGATING_INBOUND_HANDLER.name(), new DelegatingInboundHandler(connectionFacade));
            LOG.trace("TcpChannelInitializer - initChannel DelegatingInboundHandler()");
            
            if (!tlsPresent) {
                connectionFacade.fireConnectionReadyNotification();
            }
            LOG.trace("TcpChannelInitializer - initChannel End");
        } catch (Exception e) {
            LOG.warn("Failed to initialize channel", e);
            ch.close();
        }
    }

    /**
     * @return iterator through active connections
     */
    public Iterator<Channel> getConnectionIterator() {
        return allChannels.iterator();
    }

    /**
     * @return amount of active channels
     */
    public int size() {
        return allChannels.size();
    }
}
