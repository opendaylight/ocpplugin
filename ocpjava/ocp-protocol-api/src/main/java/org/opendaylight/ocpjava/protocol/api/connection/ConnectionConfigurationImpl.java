/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.connection;

import java.net.InetAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.TransportProtocol;

/**
 * @author michal.polkorab
 *
 */
public class ConnectionConfigurationImpl implements ConnectionConfiguration {

    private final InetAddress address;
    private final int port;
    private Object transferProtocol;
    private final TlsConfiguration tlsConfig;
    private final long radioHeadIdleTimeout;
    private ThreadConfiguration threadConfig;

    /**
     * Creates {@link ConnectionConfigurationImpl}
     * @param address
     * @param port
     * @param tlsConfig
     * @param radioHeadIdleTimeout
     */
    public ConnectionConfigurationImpl(InetAddress address, int port, TlsConfiguration tlsConfig, long radioHeadIdleTimeout) {
        this.address = address;
        this.port = port;
        this.tlsConfig = tlsConfig;
        this.radioHeadIdleTimeout = radioHeadIdleTimeout;
    }

    @Override
    public InetAddress getAddress() {
        return address;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public Object getTransferProtocol() {
        return transferProtocol;
    }

    /**
     * Used for testing - sets transport protocol
     * @param protocol
     */
    public void setTransferProtocol(TransportProtocol protocol) {
        this.transferProtocol = protocol;
    }

    @Override
    public long getRadioHeadIdleTimeout() {
        return radioHeadIdleTimeout;
    }

    @Override
    public Object getSslContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TlsConfiguration getTlsConfiguration() {
        return tlsConfig;
    }

    @Override
    public ThreadConfiguration getThreadConfiguration() {
        return threadConfig;
    }

    /**
     * @param threadConfig thread model configuration (configures threads used)
     */
    public void setThreadConfiguration(ThreadConfiguration threadConfig) {
        this.threadConfig = threadConfig;
    }

    @Override
    public String toString() {
        return "ConnectionConfigurationImpl [address=" + address + ", port=" + port + ", transferProtocol="
                + transferProtocol + ", radioHeadIdleTimeout=" + radioHeadIdleTimeout + ", tlsConfig=" + tlsConfig
                + ", threadConfig=" + threadConfig + "]";
    }
}
