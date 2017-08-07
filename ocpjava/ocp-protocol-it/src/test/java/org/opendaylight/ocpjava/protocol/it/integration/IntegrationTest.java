/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.it.integration;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfigurationImpl;
import org.opendaylight.ocpjava.protocol.impl.clients.ClientEvent;
import org.opendaylight.ocpjava.protocol.impl.clients.ListeningSimpleClient;
import org.opendaylight.ocpjava.protocol.impl.clients.OCPClient;
import org.opendaylight.ocpjava.protocol.impl.clients.ScenarioFactory;
import org.opendaylight.ocpjava.protocol.impl.clients.ScenarioHandler;
import org.opendaylight.ocpjava.protocol.impl.clients.SendEvent;
import org.opendaylight.ocpjava.protocol.impl.clients.SimpleClient;
import org.opendaylight.ocpjava.protocol.impl.clients.SleepEvent;
import org.opendaylight.ocpjava.protocol.impl.clients.WaitForMessageEvent;
import org.opendaylight.ocpjava.protocol.impl.core.RadioHeadConnectionProviderImpl;
import org.opendaylight.ocpjava.protocol.impl.core.TcpHandler;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionConfigurationImpl;
import org.opendaylight.ocpjava.util.ByteBufUtils;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.KeystoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.PathType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.TransportProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class IntegrationTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(IntegrationTest.class);

    private static int port;
    private TlsConfiguration tlsConfiguration;
    private static final int RADIOHEAD_IDLE_TIMEOUT = 2000;
    private static final long CONNECTION_TIMEOUT = 2000;
    private InetAddress startupAddress;
    private MockPlugin mockPlugin;
    private RadioHeadConnectionProviderImpl radioHeadConnectionProvider;
    private ConnectionConfigurationImpl connConfig;

    private Thread t;

    private enum ClientType {SIMPLE, LISTENING}
    /**
     * @param protocol communication protocol to be used during test
     * @throws Exception
     */
    public void setUp(TransportProtocol protocol) throws Exception {
        LOG.debug("\n starting test -------------------------------");

        String currentDir = System.getProperty("user.dir");
        LOG.debug("Current dir using System: {}", currentDir);
        startupAddress = InetAddress.getLocalHost();
        tlsConfiguration = null;
        if (protocol.equals(TransportProtocol.TLS)) {
            tlsConfiguration = new TlsConfigurationImpl(KeystoreType.JKS,
                    "/selfSignedRadioHead", PathType.CLASSPATH, KeystoreType.JKS,
                    "/selfSignedController", PathType.CLASSPATH) ;
        }
        connConfig = new ConnectionConfigurationImpl(startupAddress, 0, tlsConfiguration, RADIOHEAD_IDLE_TIMEOUT);
        connConfig.setTransferProtocol(protocol);
        mockPlugin = new MockPlugin();

        radioHeadConnectionProvider = new RadioHeadConnectionProviderImpl();
        radioHeadConnectionProvider.setRadioHeadConnectionHandler(mockPlugin);
        radioHeadConnectionProvider.setConfiguration(connConfig);
        radioHeadConnectionProvider.startup().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        if (protocol.equals(TransportProtocol.TCP) || protocol.equals(TransportProtocol.TLS)) {
            TcpHandler tcpHandler = (TcpHandler) radioHeadConnectionProvider.getServerFacade();
            port = tcpHandler.getPort();
        }
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        radioHeadConnectionProvider.close();
        LOG.debug("\n ending test -------------------------------");
    }

    /**
     * @param amountOfCLients
     * @param protocol true if encrypted connection should be used
     * @return new clients up and running
     * @throws ExecutionException if some client could not start
     */
    private List<OCPClient> createAndStartClient(int amountOfCLients, ScenarioHandler scenarioHandler,
            TransportProtocol protocol, ClientType clientType) throws ExecutionException {
        List<OCPClient> clientsHorde = new ArrayList<>();
        for (int i = 0; i < amountOfCLients; i++) {
            LOG.debug("startup address in createclient: {}", startupAddress.getHostAddress());
            OCPClient sc = null;
            if (clientType == ClientType.SIMPLE) {
                if (protocol.equals(TransportProtocol.TCP)) {
                    sc = new SimpleClient(startupAddress.getHostAddress(), port);
                    sc.setSecuredClient(false);
                } else if (protocol.equals(TransportProtocol.TLS)) {
                    sc = new SimpleClient(startupAddress.getHostAddress(), port);
                    sc.setSecuredClient(true);
                }
            } else if (clientType == ClientType.LISTENING) {
                sc = new ListeningSimpleClient(0);
                sc.setScenarioHandler(scenarioHandler);
                sc.setSecuredClient(false);
            } else {
                LOG.error("Unknown type of client.");
                throw new IllegalStateException("Unknown type of client.");
            }

            sc.setScenarioHandler(scenarioHandler);
            clientsHorde.add(sc);
            //sc.run();
            t = new Thread(sc);
            t.start();
        }
        for (OCPClient sc : clientsHorde) {
            try {
                sc.getIsOnlineFuture().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                LOG.error("createAndStartClient: Something borked ... ", e.getMessage(), e);
                throw new ExecutionException(e);
            }
        }
        return clientsHorde;
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInitiateConnection() throws Exception {
        setUp(TransportProtocol.TCP);
        LOG.debug("testInitiateConnection() Starting") ;

        Deque<ClientEvent> scenario = ScenarioFactory.createHandshakeScenario();
        ScenarioHandler handler = new ScenarioHandler(scenario);
        List<OCPClient> clients = createAndStartClient(1, handler, TransportProtocol.TCP, ClientType.LISTENING);
        OCPClient ocpClient = clients.get(0);
        ocpClient.getIsOnlineFuture().get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        int listeningClientPort = ((ListeningSimpleClient) ocpClient).getPort();
        mockPlugin.initiateConnection(radioHeadConnectionProvider, "localhost", listeningClientPort);
        ocpClient.getScenarioDone().get();
        LOG.debug("testInitiateConnection() Finished") ;
    }
}
