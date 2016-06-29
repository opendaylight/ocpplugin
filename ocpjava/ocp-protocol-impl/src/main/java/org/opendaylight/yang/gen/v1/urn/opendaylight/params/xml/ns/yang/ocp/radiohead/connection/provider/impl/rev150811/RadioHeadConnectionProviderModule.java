/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.radiohead.connection.provider.impl.rev150811;

import com.google.common.base.MoreObjects;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.ThreadConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.impl.core.RadioHeadConnectionProviderImpl;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.KeystoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.TransportProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RadioHeadConnectionProviderModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.radiohead.connection.provider.impl.rev150811.AbstractRadioHeadConnectionProviderModule {

    private static Logger LOG = LoggerFactory
            .getLogger(RadioHeadConnectionProviderModule.class);

    /**
     * @param identifier
     * @param dependencyResolver
     */
    public RadioHeadConnectionProviderModule(final org.opendaylight.controller.config.api.ModuleIdentifier identifier, final org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    /**
     * @param identifier
     * @param dependencyResolver
     * @param oldModule
     * @param oldInstance
     */
    public RadioHeadConnectionProviderModule(final org.opendaylight.controller.config.api.ModuleIdentifier identifier, final org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, final RadioHeadConnectionProviderModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    protected void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        LOG.info("RadioHeadConnectionProvider started.");
        RadioHeadConnectionProviderImpl radioHeadConnectionProviderImpl = new RadioHeadConnectionProviderImpl();
        try {
            ConnectionConfiguration connConfiguration = createConnectionConfiguration();
            radioHeadConnectionProviderImpl.setConfiguration(connConfiguration);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return radioHeadConnectionProviderImpl;
    }

    /**
     * @return instance configuration object
     * @throws UnknownHostException
     */
    private ConnectionConfiguration createConnectionConfiguration() throws UnknownHostException {
        final InetAddress address = extractIpAddressBin(getAddress());
        final Integer port = getPort();
        final long radioHeadIdleTimeout = getRadioHeadIdleTimeout();
        final Tls tlsConfig = getTls();
        final Threads threads = getThreads();
        final TransportProtocol transportProtocol = getTransportProtocol();

        return new ConnectionConfiguration() {
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
                return transportProtocol;
            }
            @Override
            public TlsConfiguration getTlsConfiguration() {
                if (tlsConfig == null || !(TransportProtocol.TLS.equals(transportProtocol))) {
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
            public ThreadConfiguration getThreadConfiguration() {
                if (threads == null) {
                    return null;
                }
                return new ThreadConfiguration() {

                    @Override
                    public int getWorkerThreadCount() {
                        return threads.getWorkerThreads();
                    }

                    @Override
                    public int getBossThreadCount() {
                        return threads.getBossThreads();
                    }
                };
            }
        };
    }

    /**
     * @param address
     * @return
     * @throws UnknownHostException
     */
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

    /**
     * @param value
     * @return
     */
    private static byte[] address2bin(final String value) {
        //TODO: translate ipv4 or ipv6 into byte[]
        return null;
    }
}
