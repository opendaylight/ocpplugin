/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection;

import java.net.InetSocketAddress;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueueHandlerRegistration;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.OutboundQueueProvider;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceDisconnectedHandler;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;

/**
 * Test for ConnectionContextImpl.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectionContextImplTest {

    @Mock
    private ConnectionAdapter connetionAdapter;
    @Mock
    private OutboundQueueHandlerRegistration<OutboundQueueProvider> outboundQueueRegistration;
    @Mock
    private DeviceDisconnectedHandler deviceDisconnectedHandler;

    private ConnectionContextImpl connectionContext;

    @Before
    public void setUp() throws Exception {
        Mockito.when(connetionAdapter.getRemoteAddress()).thenReturn(InetSocketAddress.createUnresolved("ocp-ut.example.org", 4242));
        Mockito.when(connetionAdapter.isAlive()).thenReturn(true);

        connectionContext = new ConnectionContextImpl(connetionAdapter);
        connectionContext.setNodeId(new NodeId("ut-node:123"));
        connectionContext.setOutboundQueueHandleRegistration(outboundQueueRegistration);
        connectionContext.setDeviceDisconnectedHandler(deviceDisconnectedHandler);

        Assert.assertNull(connectionContext.getConnectionState());
    }

    @Test
    public void testCloseConnection1() throws Exception {
        connectionContext.closeConnection(true);
        Mockito.verify(outboundQueueRegistration).close();
        Mockito.verify(connetionAdapter).disconnect();
        Assert.assertEquals(ConnectionContext.CONNECTION_STATE.CLOSED, connectionContext.getConnectionState());

        Mockito.verify(deviceDisconnectedHandler).onDeviceDisconnected(connectionContext);
    }

    @Test
    public void testCloseConnection2() throws Exception {
        connectionContext.closeConnection(false);
        Mockito.verify(outboundQueueRegistration).close();
        Mockito.verify(connetionAdapter).disconnect();
        Assert.assertEquals(ConnectionContext.CONNECTION_STATE.CLOSED, connectionContext.getConnectionState());

        Mockito.verify(deviceDisconnectedHandler, Mockito.never()).onDeviceDisconnected(connectionContext);
    }

    @Test
    public void testOnConnectionClosed() throws Exception {
        connectionContext.onConnectionClosed();
        Assert.assertEquals(ConnectionContext.CONNECTION_STATE.CLOSED, connectionContext.getConnectionState());
        Mockito.verify(outboundQueueRegistration).close();
        Assert.assertEquals(ConnectionContext.CONNECTION_STATE.CLOSED, connectionContext.getConnectionState());
        Mockito.verify(deviceDisconnectedHandler).onDeviceDisconnected(connectionContext);
    }

    @Test
    public void testChangeStateToHelloWait() throws Exception {
        connectionContext.changeStateToHelloWait();
        Assert.assertEquals(ConnectionContext.CONNECTION_STATE.HELLO_WAIT, connectionContext.getConnectionState());
    }

    @Test
    public void testChangeStateToEstablished() throws Exception {
        connectionContext.changeStateToEstablished();
        Assert.assertEquals(ConnectionContext.CONNECTION_STATE.ESTABLISHED, connectionContext.getConnectionState());
    }

    @Test
    public void testChangeStateToMaintenance() throws Exception {
        connectionContext.changeStateToMaintenance();
        Assert.assertEquals(ConnectionContext.CONNECTION_STATE.MAINTENANCE, connectionContext.getConnectionState());
    }
}
