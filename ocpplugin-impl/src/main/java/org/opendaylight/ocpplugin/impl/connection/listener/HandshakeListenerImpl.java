/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection.listener;

import java.net.InetSocketAddress;
import java.util.List;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeContext;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceConnectedHandler;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeListener;
import org.opendaylight.ocpplugin.impl.util.InventoryDataServiceUtil;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class HandshakeListenerImpl implements HandshakeListener {

    private static final Logger LOG = LoggerFactory.getLogger(HandshakeListenerImpl.class);

    private ConnectionContext connectionContext;
    private DeviceConnectedHandler deviceConnectedHandler;
    private HandshakeContext handshakeContext;

    /**
     * @param connectionContext
     * @param deviceConnectedHandler
     */
    public HandshakeListenerImpl(ConnectionContext connectionContext, DeviceConnectedHandler deviceConnectedHandler) {
        this.connectionContext = connectionContext;
        this.deviceConnectedHandler = deviceConnectedHandler;
    }

    @Override
    public void onHandshakeSuccessfull(HelloMessage hello) {
        String radioheadId = hello.getVendorId() + "-" + hello.getSerialNumber();
        closeHandshakeContext();
        connectionContext.changeStateToEstablished();
        LOG.debug("handshake succeeded: {}", radioheadId);
        connectionContext.setNodeId(InventoryDataServiceUtil.nodeIdFromRadioheadId(radioheadId));
        deviceConnectedHandler.deviceConnected(connectionContext);
    }

    @Override
    public void onHandshakeFailure(ConnectionContext.CONNECTION_STATE nextState) {
        LOG.debug("handshake failed: {}  next state: {}", 
                  connectionContext.getConnectionAdapter().getRemoteAddress(), nextState);
        closeHandshakeContext();
        if (ConnectionContext.CONNECTION_STATE.MAINTENANCE.equals(nextState))
            connectionContext.changeStateToMaintenance();
        else if (ConnectionContext.CONNECTION_STATE.CLOSED.equals(nextState))
            connectionContext.closeConnection(false);
    }

    private void closeHandshakeContext() {
        try {
            handshakeContext.close();
        } catch (Exception e) {
            LOG.warn("Closing handshake context failed: {}", e.getMessage());
            LOG.debug("Detail in hanshake context close:", e);
        }
    }

    @Override
    public void setHandshakeContext(HandshakeContext handshakeContext) {
        this.handshakeContext = handshakeContext;
    }
}
