/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionConfigurationImpl;
import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfigurationImpl;
import org.opendaylight.ocpjava.protocol.impl.core.RadioHeadConnectionProviderImpl;
import org.opendaylight.ocpjava.protocol.spi.connection.RadioHeadConnectionProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.KeystoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.PathType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.TransportProtocol;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * @author michal.polkorab
 *
 */
public class RadioHeadConnectionProviderImplTest {

    @Mock RadioHeadConnectionHandler handler;

    private static final int RADIOHEAD_IDLE_TIMEOUT = 2000;
    private static final int WAIT_TIMEOUT = 2000;
    private InetAddress startupAddress;
    private TlsConfiguration tlsConfiguration;
    private RadioHeadConnectionProviderImpl provider;
    private ConnectionConfigurationImpl config;

    /**
     * Creates new {@link RadioHeadConnectionProvider} instance for each test
     * @param protocol communication protocol
     */
    public void startUp(TransportProtocol protocol) {
        MockitoAnnotations.initMocks(this);
        config = null;
        if (protocol != null) {
            createConfig(protocol);
        }
        provider = new RadioHeadConnectionProviderImpl();
    }

    private void createConfig(TransportProtocol protocol) {
        try {
            startupAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        tlsConfiguration = null;
        if (protocol.equals(TransportProtocol.TLS)) {
            tlsConfiguration = new TlsConfigurationImpl(KeystoreType.JKS,
                    "/selfSignedRadioHead", PathType.CLASSPATH, KeystoreType.JKS,
                    "/selfSignedController", PathType.CLASSPATH) ;
        }
        config = new ConnectionConfigurationImpl(startupAddress, 0, tlsConfiguration, RADIOHEAD_IDLE_TIMEOUT);
        config.setTransferProtocol(protocol);
    }

    /**
     * Tests provider startup - without configuration and {@link RadioHeadConnectionHandler}
     */
    @Test
    public void testStartup1() {
        provider = new RadioHeadConnectionProviderImpl();
        ListenableFuture<Boolean> future = provider.startup();
        try {
            future.get(WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Assert.assertEquals("Wrong state", "java.lang.NullPointerException", e.getMessage());
        }
    }

    /**
     * Tests provider startup - without configuration
     */
    @Test
    public void testStartup2() {
        startUp(null);
        provider.setRadioHeadConnectionHandler(handler);
        ListenableFuture<Boolean> future = provider.startup();
        try {
            future.get(WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Assert.assertEquals("Wrong state", "java.lang.NullPointerException", e.getMessage());
        }
    }

    /**
     * Tests provider startup - without {@link RadioHeadConnectionHandler}
     */
    @Test
    public void testStartup3() {
        startUp(TransportProtocol.TCP);
        provider.setConfiguration(config);
        ListenableFuture<Boolean> future = provider.startup();
        try {
            future.get(WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Assert.assertEquals("Wrong state", "java.lang.IllegalStateException:"
                    + " RadioHeadConnectionHandler is not set", e.getMessage());
        }
    }

    /**
     * Tests correct provider startup - over TCP
     */
    @Test
    public void testStartup4() {
        startUp(TransportProtocol.TCP);
        provider.setConfiguration(config);
        provider.setRadioHeadConnectionHandler(handler);
        try {
            Assert.assertTrue("Failed to start", provider.startup().get(WAIT_TIMEOUT, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Assert.fail();
        }
    }

    /**
     * Tests correct provider startup - over TLS
     */
    @Test
    public void testStartup5() {
        startUp(TransportProtocol.TLS);
        provider.setConfiguration(config);
        provider.setRadioHeadConnectionHandler(handler);
        try {
            Assert.assertTrue("Failed to start", provider.startup().get(WAIT_TIMEOUT, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Assert.fail();
        }
    }

    /**
     * Tests correct provider shutdown
     */
    @Test
    public void testShutdown() {
        startUp(TransportProtocol.TCP);
        provider.setConfiguration(config);
        provider.setRadioHeadConnectionHandler(handler);
        try {
            Assert.assertTrue("Failed to start", provider.startup().get(WAIT_TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue("Failed to stop", provider.shutdown().get(5 * WAIT_TIMEOUT, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
