/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializationFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializationFactory;

/**
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class ChannelInitializerFactory {

    private long radioHeadIdleTimeOut;
    private DeserializationFactory deserializationFactory;
    private SerializationFactory serializationFactory;
    private TlsConfiguration tlsConfig;
    private RadioHeadConnectionHandler radioHeadConnectionHandler;

    /**
     * @return PublishingChannelInitializer that initializes new channels
     */
    public TcpChannelInitializer createPublishingChannelInitializer() {
        TcpChannelInitializer initializer = new TcpChannelInitializer();
        initializer.setRadioHeadIdleTimeout(radioHeadIdleTimeOut);
        initializer.setDeserializationFactory(deserializationFactory);
        initializer.setSerializationFactory(serializationFactory);
        initializer.setTlsConfiguration(tlsConfig);
        initializer.setRadioHeadConnectionHandler(radioHeadConnectionHandler);
        return initializer;
    }


    /**
     * @param radioHeadIdleTimeOut
     */
    public void setRadioHeadIdleTimeout(long radioHeadIdleTimeOut) {
        this.radioHeadIdleTimeOut = radioHeadIdleTimeOut;
    }

    /**
     * @param deserializationFactory
     */
    public void setDeserializationFactory(DeserializationFactory deserializationFactory) {
        this.deserializationFactory = deserializationFactory;
    }

    /**
     * @param serializationFactory
     */
    public void setSerializationFactory(SerializationFactory serializationFactory) {
        this.serializationFactory = serializationFactory;
    }

    /**
     * @param tlsConfig
     */
    public void setTlsConfig(TlsConfiguration tlsConfig) {
        this.tlsConfig = tlsConfig;
    }

    /**
     * @param radioHeadConnectionHandler
     */
    public void setRadioHeadConnectionHandler(RadioHeadConnectionHandler radioHeadConnectionHandler) {
        this.radioHeadConnectionHandler = radioHeadConnectionHandler;
    }
}