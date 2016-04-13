/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializationFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializationFactory;

/**
 * @param <C> Channel type
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public abstract class ProtocolChannelInitializer<C extends Channel>
        extends ChannelInitializer<C> {

    private RadioHeadConnectionHandler radioHeadConnectionHandler;
    private long radioHeadIdleTimeout;
    private SerializationFactory serializationFactory;
    private DeserializationFactory deserializationFactory;
    private TlsConfiguration tlsConfiguration;

    /**
     * @param radioHeadConnectionHandler the radioHeadConnectionHandler to set
     */
    public void setRadioHeadConnectionHandler(final RadioHeadConnectionHandler radioHeadConnectionHandler) {
        this.radioHeadConnectionHandler = radioHeadConnectionHandler;
    }

    /**
     * @param radioHeadIdleTimeout the radioHeadIdleTimeout to set
     */
    public void setRadioHeadIdleTimeout(final long radioHeadIdleTimeout) {
        this.radioHeadIdleTimeout = radioHeadIdleTimeout;
    }

    /**
     * @param serializationFactory
     */
    public void setSerializationFactory(final SerializationFactory serializationFactory) {
        this.serializationFactory = serializationFactory;
    }

    /**
     * @param deserializationFactory
     */
    public void setDeserializationFactory(final DeserializationFactory deserializationFactory) {
        this.deserializationFactory = deserializationFactory;
    }

    /**
     * @param tlsConfiguration
     */
    public void setTlsConfiguration(TlsConfiguration tlsConfiguration) {
        this.tlsConfiguration = tlsConfiguration;
    }

    /**
     * @return radioHead connection handler
     */
    public RadioHeadConnectionHandler getRadioHeadConnectionHandler() {
        return radioHeadConnectionHandler;
    }

    /**
     * @return radioHead idle timeout
     */
    public long getRadioHeadIdleTimeout() {
        return radioHeadIdleTimeout;
    }

    /**
     * @return serialization factory
     */
    public SerializationFactory getSerializationFactory() {
        return serializationFactory;
    }

    /**
     * @return deserialization factory
     */
    public DeserializationFactory getDeserializationFactory() {
        return deserializationFactory;
    }

    /**
     * @return TLS configuration
     */
    public TlsConfiguration getTlsConfiguration() {
        return tlsConfiguration;
    }
}
