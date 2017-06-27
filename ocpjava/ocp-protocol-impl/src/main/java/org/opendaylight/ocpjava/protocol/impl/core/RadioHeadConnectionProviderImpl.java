/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */


package org.opendaylight.ocpjava.protocol.impl.core;

import io.netty.channel.nio.NioEventLoopGroup;

import org.opendaylight.ocpjava.protocol.api.connection.ConnectionConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;
import org.opendaylight.ocpjava.protocol.api.extensibility.DeserializerRegistry;
import org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistry;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializationFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializerRegistryImpl;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializationFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializerRegistryImpl;
import org.opendaylight.ocpjava.protocol.spi.connection.RadioHeadConnectionProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.TransportProtocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Exposed class for server handling<br>
 * @author mirehak
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class RadioHeadConnectionProviderImpl implements RadioHeadConnectionProvider, ConnectionInitializer {

    private static final Logger LOG = LoggerFactory
            .getLogger(RadioHeadConnectionProviderImpl.class);
    private RadioHeadConnectionHandler radioHeadConnectionHandler;
    private ServerFacade serverFacade;
    private ConnectionConfiguration connConfig;
    private SerializationFactory serializationFactory;
    private SerializerRegistry serializerRegistry;
    private DeserializerRegistry deserializerRegistry;
    private DeserializationFactory deserializationFactory;
    private TcpConnectionInitializer connectionInitializer;

    /** Constructor */
    public RadioHeadConnectionProviderImpl() {
        serializerRegistry = new SerializerRegistryImpl();
        serializerRegistry.init();
        serializationFactory = new SerializationFactory();
        serializationFactory.setSerializerTable(serializerRegistry);
        deserializerRegistry = new DeserializerRegistryImpl();
        deserializerRegistry.init();
        deserializationFactory = new DeserializationFactory();
        deserializationFactory.setRegistry(deserializerRegistry);
    }

    @Override
    public void setConfiguration(ConnectionConfiguration connConfig) {
        this.connConfig = connConfig;
    }

    @Override
    public void setRadioHeadConnectionHandler(RadioHeadConnectionHandler radioHeadConnectionHandler) {
        LOG.debug("setRadioHeadConnectionHandler");
        this.radioHeadConnectionHandler = radioHeadConnectionHandler;
    }

    @Override
    public ListenableFuture<Boolean> shutdown() {
        LOG.debug("Shutdown summoned");
        if(serverFacade == null){
            LOG.warn("Can not shutdown - not configured or started");
            throw new IllegalStateException("RadioHeadConnectionProvider is not started or not configured.");
        }
        return serverFacade.shutdown();
    }

    @Override
    public ListenableFuture<Boolean> startup() {
        LOG.debug("Startup summoned");
        ListenableFuture<Boolean> result = null;
        try {
            serverFacade = createAndConfigureServer();
            if (radioHeadConnectionHandler == null) {
                throw new IllegalStateException("RadioHeadConnectionHandler is not set");
            }
            new Thread(serverFacade).start();
            result = serverFacade.getIsOnlineFuture();
        } catch (Exception e) {
            SettableFuture<Boolean> exResult = SettableFuture.create();
            exResult.setException(e);
            result = exResult;
        }
        return result;
    }

    /**
     * @return
     */
    private ServerFacade createAndConfigureServer() {
        LOG.debug("Configuring ..");
        ServerFacade server = null;
        ChannelInitializerFactory factory = new ChannelInitializerFactory();
        factory.setRadioHeadConnectionHandler(radioHeadConnectionHandler);
        factory.setRadioHeadIdleTimeout(connConfig.getRadioHeadIdleTimeout());
        factory.setTlsConfig(connConfig.getTlsConfiguration());
        factory.setSerializationFactory(serializationFactory);
        factory.setDeserializationFactory(deserializationFactory);
        TransportProtocol transportProtocol = (TransportProtocol) connConfig.getTransferProtocol();
        if (transportProtocol.equals(TransportProtocol.TCP) || transportProtocol.equals(TransportProtocol.TLS)) {
            server = new TcpHandler(connConfig.getAddress(), connConfig.getPort());
            TcpChannelInitializer channelInitializer = factory.createPublishingChannelInitializer();
            ((TcpHandler) server).setChannelInitializer(channelInitializer);
            ((TcpHandler) server).initiateEventLoopGroups(connConfig.getThreadConfiguration());

            NioEventLoopGroup workerGroupFromTcpHandler = ((TcpHandler) server).getWorkerGroup();
            connectionInitializer = new TcpConnectionInitializer(workerGroupFromTcpHandler);
            connectionInitializer.setChannelInitializer(channelInitializer);
            connectionInitializer.run();
        } else {
            throw new IllegalStateException("Unknown transport protocol received: " + transportProtocol);
        }
        server.setThreadConfig(connConfig.getThreadConfiguration());
        return server;
    }

    /**
     * @return servers
     */
    public ServerFacade getServerFacade() {
        return serverFacade;
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    @Override
    public void initiateConnection(String host, int port) {
        connectionInitializer.initiateConnection(host, port);
    }

}
