/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection.listener;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opendaylight.ocpplugin.api.OcpConstants;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.impl.connection.ConnectionContextImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SalDeviceMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.DisconnectEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.DisconnectEventBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.RadioHeadIdleEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.RadioHeadIdleEventBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.SystemNotificationsListener;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

/**
 * Testing basic bahavior of SystemNotificationsListenerImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class SystemNotificationsListenerImplTest {

    public static final int SAFE_TIMEOUT = 1000;
    @Mock
    private org.opendaylight.controller.sal.binding.api.RpcConsumerRegistry rpcRegistry;    
    @Mock
    private SalDeviceMgmtService salDeviceMgmtService;
    @Mock
    private org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter connectionAdapter;
    @Mock
    private ConnectionContext connectionContext;

    private SystemNotificationsListenerImpl systemNotificationsListener;
    private ConnectionContextImpl connectionContextGolem;
    private static final NodeId nodeId = new NodeId("OCP:TEST");

    @Before
    public void setUp() {
        connectionContextGolem = new ConnectionContextImpl(connectionAdapter);
        connectionContextGolem.changeStateToEstablished();
        connectionContextGolem.setNodeId(nodeId);
        connectionContext = Mockito.spy(connectionContextGolem);

        Mockito.when(connectionAdapter.getRemoteAddress()).thenReturn(
                InetSocketAddress.createUnresolved("unit-odl.example.org", 4242));
        connectionContext = Mockito.spy(connectionContextGolem);

        Mockito.when(connectionContext.getConnectionAdapter()).thenReturn(connectionAdapter);
        //Mockito.when(connectionContext.getNodeId()).thenReturn(nodeId);
        //Mockito.when(connectionContext.getConnectionState()).thenReturn(ConnectionContext.CONNECTION_STATE.ESTABLISHED);
        Mockito.when(rpcRegistry.getRpcService(SalDeviceMgmtService.class)).thenReturn(salDeviceMgmtService);

        systemNotificationsListener = new SystemNotificationsListenerImpl(connectionContext, rpcRegistry);
    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(connectionContext);
    }

    /**
     * successful scenario - connection is on and closes without errors
     *
     * @throws Exception
     */
    @Test
    public void testOnDisconnectEvent1() throws Exception {
        Mockito.when(connectionAdapter.isAlive()).thenReturn(true);
        Mockito.when(connectionAdapter.disconnect()).thenReturn(Futures.immediateFuture(Boolean.TRUE));

        DisconnectEvent disconnectNotification = new DisconnectEventBuilder().setInfo("testing disconnect").build();
        systemNotificationsListener.onDisconnectEvent(disconnectNotification);

        Mockito.verify(connectionContext, Mockito.timeout(SAFE_TIMEOUT).atLeastOnce()).getConnectionState();
        Mockito.verify(connectionContext).onConnectionClosed();

    }

    /**
     * broken scenario - connection is on but fails to close
     *
     * @throws Exception
     */
    @Test
    public void testOnDisconnectEvent2() throws Exception {
        Mockito.when(connectionAdapter.isAlive()).thenReturn(true);
        Mockito.when(connectionAdapter.disconnect()).thenReturn(Futures.immediateFuture(Boolean.FALSE));

        DisconnectEvent disconnectNotification = new DisconnectEventBuilder().setInfo("testing disconnect").build();
        systemNotificationsListener.onDisconnectEvent(disconnectNotification);

        Mockito.verify(connectionContext, Mockito.timeout(SAFE_TIMEOUT).atLeastOnce()).getConnectionState();
        Mockito.verify(connectionContext).onConnectionClosed();

    }

    /**
     * successful scenario - connection is already down
     *
     * @throws Exception
     */
    @Test
    public void testOnDisconnectEvent3() throws Exception {
        Mockito.when(connectionAdapter.isAlive()).thenReturn(true);
        Mockito.when(connectionAdapter.disconnect()).thenReturn(Futures.<Boolean>immediateFailedFuture(new Exception("unit exception")));

        DisconnectEvent disconnectNotification = new DisconnectEventBuilder().setInfo("testing disconnect").build();
        systemNotificationsListener.onDisconnectEvent(disconnectNotification);

        Mockito.verify(connectionContext, Mockito.timeout(SAFE_TIMEOUT).atLeastOnce()).getConnectionState();
        Mockito.verify(connectionContext).onConnectionClosed();
    }

    /**
     * broken scenario - connection is on but throws error on close
     *
     * @throws Exception
     */
    @Test
    public void testOnDisconnectEvent4() throws Exception {
        Mockito.when(connectionAdapter.isAlive()).thenReturn(false);

        DisconnectEvent disconnectNotification = new DisconnectEventBuilder().setInfo("testing disconnect").build();
        systemNotificationsListener.onDisconnectEvent(disconnectNotification);

        Mockito.verify(connectionContext, Mockito.timeout(SAFE_TIMEOUT).atLeastOnce()).getConnectionState();
        Mockito.verify(connectionContext).onConnectionClosed();
   }

    /**
     * first encounter of idle event, health check reply received successfully
     *
     * @throws Exception
     */
    @Test
    public void testOnRadioHeadIdleEvent1() throws Exception {
        final SettableFuture<RpcResult<HealthCheckOutput>> healthCheckReply = SettableFuture.create();
        Mockito.when(salDeviceMgmtService.healthCheck(Matchers.any(HealthCheckInput.class))).thenReturn(healthCheckReply);

        RadioHeadIdleEvent notification = new RadioHeadIdleEventBuilder().setInfo("wake up, device sleeps").build();
        systemNotificationsListener.onRadioHeadIdleEvent(notification);

        // make sure that the idle notification processing thread started
        Thread.sleep(SAFE_TIMEOUT);
        HealthCheckOutput healthCheckOutput = new HealthCheckOutputBuilder().setResult(OriRes.SUCCESS).build();
        healthCheckReply.set(RpcResultBuilder.success(healthCheckOutput).build());

        Mockito.verify(connectionContext, Mockito.timeout(SAFE_TIMEOUT).atLeastOnce()).getConnectionState();
        Mockito.verify(connectionContext, Mockito.timeout(SAFE_TIMEOUT).atLeastOnce()).getNodeId();
        Mockito.verify(salDeviceMgmtService, Mockito.timeout(SAFE_TIMEOUT)).healthCheck(Matchers.any(HealthCheckInput.class));
        Mockito.verify(connectionAdapter, Mockito.never()).disconnect();
    }

    /**
     * first encounter of idle event, health check reply not received
     *
     * @throws Exception
     */
    @Test
    public void testOnRadioHeadIdleEvent2() throws Exception {
        final SettableFuture<RpcResult<HealthCheckOutput>> healthCheckReply = SettableFuture.create();
        Mockito.when(salDeviceMgmtService.healthCheck(Matchers.any(HealthCheckInput.class))).thenReturn(healthCheckReply);
        Mockito.when(connectionAdapter.isAlive()).thenReturn(true);
        Mockito.when(connectionAdapter.disconnect()).thenReturn(Futures.<Boolean>immediateFailedFuture(new Exception("unit exception")));

        RadioHeadIdleEvent notification = new RadioHeadIdleEventBuilder().setInfo("wake up, device sleeps").build();
        systemNotificationsListener.onRadioHeadIdleEvent(notification);

        Thread.sleep(OcpConstants.MAX_RPC_REPLY_TIMEOUT + SAFE_TIMEOUT);

        Mockito.verify(connectionContext, Mockito.timeout(SAFE_TIMEOUT).atLeastOnce()).getConnectionState();
        Mockito.verify(connectionContext, Mockito.timeout(SAFE_TIMEOUT).atLeastOnce()).getNodeId();
        Mockito.verify(salDeviceMgmtService, Mockito.timeout(SAFE_TIMEOUT)).healthCheck(Matchers.any(HealthCheckInput.class));
    }

}
