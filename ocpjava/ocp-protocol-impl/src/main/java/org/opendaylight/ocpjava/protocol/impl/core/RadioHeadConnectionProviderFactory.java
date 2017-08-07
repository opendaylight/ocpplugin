/*
 * Copyright (c) 2017 Inocybe Technologies and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpjava.protocol.impl.core;

import com.google.common.base.MoreObjects;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionConfigurationImpl;
import org.opendaylight.ocpjava.protocol.api.connection.ThreadConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.spi.connection.RadioHeadConnectionProvider;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.KeystoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.TransportProtocol;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.radiohead.config.rev170807.RadioheadConnectionConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.radiohead.config.rev170807.radiohead.connection.config.Threads;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.radiohead.config.rev170807.radiohead.connection.config.Tls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating RadioHeadConnectionProvider instances.
 *
 * @author Thomas Pantelis
 */
public class RadioHeadConnectionProviderFactory {
    private static final Logger LOG = LoggerFactory.getLogger(RadioHeadConnectionProviderFactory.class);

    public RadioHeadConnectionProvider newInstance(final RadioheadConnectionConfig config) {
        InetAddress address;
        try {
            address = extractIpAddressBin(config.getAddress());
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        final int port = config.getPort();
        final long radioHeadIdleTimeout = config.getRadioHeadIdleTimeout();

        ConnectionConfigurationImpl connectionConfig = new ConnectionConfigurationImpl(address, port,
                getTlsConfiguration(config), radioHeadIdleTimeout);
        connectionConfig.setTransferProtocol(config.getTransportProtocol());

        final Threads threads = config.getThreads();
        if (threads != null) {
            connectionConfig.setThreadConfiguration(new ThreadConfiguration() {
                @Override
                public int getWorkerThreadCount() {
                    return threads.getWorkerThreads();
                }

                @Override
                public int getBossThreadCount() {
                    return threads.getBossThreads();
                }
            });
        }

        RadioHeadConnectionProviderImpl provider = new RadioHeadConnectionProviderImpl();
        provider.setConfiguration(connectionConfig);

        LOG.info("Instantiated RadioHeadConnectionProvider with config: {}", connectionConfig);
        return provider;
    }

    private TlsConfiguration getTlsConfiguration(final RadioheadConnectionConfig config) {
        final Tls tlsConfig = config.getTls();
        if (tlsConfig == null || !TransportProtocol.TLS.equals(config.getTransportProtocol())) {
            return null;
        }

        return new TlsConfiguration() {
            @Override
            public KeystoreType getTlsTruststoreType() {
                return MoreObjects.firstNonNull(tlsConfig.getTruststoreType(), null);
            }
            @Override
            public String getTlsTruststore() {
                return MoreObjects.firstNonNull(tlsConfig.getTruststore(), null);
            }
            @Override
            public KeystoreType getTlsKeystoreType() {
                return MoreObjects.firstNonNull(tlsConfig.getKeystoreType(), null);
            }
            @Override
            public String getTlsKeystore() {
                return MoreObjects.firstNonNull(tlsConfig.getKeystore(), null);
            }
            @Override
            public org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.PathType getTlsKeystorePathType() {
                return MoreObjects.firstNonNull(tlsConfig.getKeystorePathType(), null);
            }
            @Override
            public org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.PathType getTlsTruststorePathType() {
                return MoreObjects.firstNonNull(tlsConfig.getTruststorePathType(), null);
            }
            @Override
            public String getKeystorePassword() {
                return MoreObjects.firstNonNull(tlsConfig.getKeystorePassword(), null);
            }
            @Override
            public String getCertificatePassword() {
                return MoreObjects.firstNonNull(tlsConfig.getCertificatePassword(), null);
            }
            @Override
            public String getTruststorePassword() {
                return MoreObjects.firstNonNull(tlsConfig.getTruststorePassword(), null);
            }
        };
    }

    private static InetAddress extractIpAddressBin(final IpAddress address) throws UnknownHostException {
        byte[] addressBin = null;
        if (address != null) {
            if (address.getIpv4Address() != null) {
                addressBin = address2bin(address.getIpv4Address().getValue());
            } else if (address.getIpv6Address() != null) {
                addressBin = address2bin(address.getIpv6Address().getValue());
            }
        }

        if (addressBin == null) {
            return null;
        } else {
            return InetAddress.getByAddress(addressBin);
        }
    }

    private static byte[] address2bin(final String value) {
        //TODO: translate ipv4 or ipv6 into byte[]
        return null;
    }
}
