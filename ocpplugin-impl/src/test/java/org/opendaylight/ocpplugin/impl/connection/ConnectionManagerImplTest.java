/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection;

import com.google.common.util.concurrent.SettableFuture;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionReadyListener;
import org.opendaylight.ocpplugin.api.OcpConstants;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceConnectedHandler;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessageBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionListener;
import org.opendaylight.controller.sal.binding.api.RpcConsumerRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SalDeviceMgmtService;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

/**
 * test of ConnectionManagerImpl - lightweight version, using basic ways (TDD)
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectionManagerImplTest {

    /** timeout of final step [ms] */
    private static final int FINAL_STEP_TIMEOUT = 500;
    private ConnectionManagerImpl connectionManagerImpl;
    @Mock
    private RpcConsumerRegistry rpcRegistry;
    @Mock
    private SalDeviceMgmtService salDeviceMgmtService;
    @Mock
    private ConnectionAdapter connection;
    @Mock
    private DeviceConnectedHandler deviceConnectedHandler;
    @Captor
    private ArgumentCaptor<ConnectionReadyListener> connectionReadyListenerAC;
    @Captor
    private ArgumentCaptor<OcpExtensionListener> ocpExtListenerAC;

    /**
     * before each test method
     */
    @Before
    public void setUp() {
        connectionManagerImpl = new ConnectionManagerImpl(rpcRegistry);
        connectionManagerImpl.setDeviceConnectedHandler(deviceConnectedHandler);
        final InetSocketAddress deviceAddress = InetSocketAddress.createUnresolved("yahoo", 42);
        Mockito.when(connection.getRemoteAddress()).thenReturn(deviceAddress);
        Mockito.when(connection.isAlive()).thenReturn(true);
        Mockito.when(rpcRegistry.getRpcService(SalDeviceMgmtService.class)).thenReturn(salDeviceMgmtService);
    }

    /**
     * after each test method
     * @throws InterruptedException
     */
    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(200L);
    }

    /**
     * Test method for org.opendaylight.ocpplugin.impl.connection.ConnectionManagerImpl#onRadioHeadConnected(org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter).
     * invoking onConnectionReady first, scenario:
     *   receive hello from device (ocp version is correct)
     *   send hello ack to device
     * @throws InterruptedException
     */
    @Test
    public void testOnRadioHeadConnected1() throws InterruptedException {
        connectionManagerImpl.onRadioHeadConnected(connection);
        Mockito.verify(connection).setConnectionReadyListener(connectionReadyListenerAC.capture());
        Mockito.verify(connection).setMessageExtListener(ocpExtListenerAC.capture());

        // prepare void reply (hello rpc output)
        final SettableFuture<RpcResult<Void>> voidResponseFx = SettableFuture.<RpcResult<Void>>create();
        Mockito.when(connection.hello(Matchers.any(HelloInput.class))).thenReturn(voidResponseFx);

        // fire handshake
        connectionReadyListenerAC.getValue().onConnectionReady();

        // send hello with correct ocp version
        final HelloMessage hello = new HelloMessageBuilder()
        .setVersion(OcpConstants.OCP_VERSION)
        .setVendorId("XYZ")
        .setSerialNumber("123")
        .build();
        ocpExtListenerAC.getValue().onHelloMessage(hello);

        // deliver hello ack send output (void)
        Thread.sleep(100L);
        final RpcResult<Void> helloResponse = RpcResultBuilder.success((Void) null).build();
        voidResponseFx.set(helloResponse);

        Mockito.verify(deviceConnectedHandler, Mockito.timeout(FINAL_STEP_TIMEOUT)).deviceConnected(Matchers.any(ConnectionContext.class));
    }

    /**
     * Test method for org.opendaylight.ocpplugin.impl.connection.ConnectionManagerImpl#onRadioHeadConnected(org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter).
     * invoking onConnectionReady first, scenario:
     *   receive hello from device (ocp version is incorrect)
     *   send hello ack to device
     * @throws InterruptedException
     */
    @Test
    public void testOnRadioHeadConnected2() throws InterruptedException {
        connectionManagerImpl.onRadioHeadConnected(connection);
        Mockito.verify(connection).setConnectionReadyListener(connectionReadyListenerAC.capture());
        Mockito.verify(connection).setMessageExtListener(ocpExtListenerAC.capture());

        // prepare void reply (hello rpc output)
        final SettableFuture<RpcResult<Void>> voidResponseFx = SettableFuture.<RpcResult<Void>>create();
        Mockito.when(connection.hello(Matchers.any(HelloInput.class))).thenReturn(voidResponseFx);

        // fire handshake
        connectionReadyListenerAC.getValue().onConnectionReady();

        // send hello with correct ocp version
        final HelloMessage hello = new HelloMessageBuilder()
        .setVersion("unknown")
        .setVendorId("XYZ")
        .setSerialNumber("123")
        .build();
        ocpExtListenerAC.getValue().onHelloMessage(hello);

        // deliver hello ack send output (void)
        Thread.sleep(100L);
        final RpcResult<Void> helloResponse = RpcResultBuilder.success((Void) null).build();
        voidResponseFx.set(helloResponse);

        Mockito.verify(deviceConnectedHandler, Mockito.timeout(500)).deviceConnected(Matchers.any(ConnectionContext.class));
    }

}
