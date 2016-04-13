/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation 
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueue;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueueHandlerRegistration;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.OutboundQueueProvider;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceDisconnectedHandler;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.ocpplugin.api.OcpConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class ConnectionContextImpl implements ConnectionContext {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionContextImpl.class);

    private final ConnectionAdapter connectionAdapter;
    private CONNECTION_STATE connectionState;
    private NodeId nodeId;
    private DeviceDisconnectedHandler deviceDisconnectedHandler;
    private OutboundQueueProvider outboundQueueProvider;
    private OutboundQueueHandlerRegistration<OutboundQueueProvider> outboundQueueHandlerRegistration;
    private XsdUnsignedShort tlmTimeout = OcpConstants.DEFAULT_TLM_TIMEOUT; 

    /**
     * @param connectionAdapter
     */
    public ConnectionContextImpl(final ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    @Override
    public ConnectionAdapter getConnectionAdapter() {
        return connectionAdapter;
    }

    @Override
    public OutboundQueue getOutboundQueueProvider() {
        return this.outboundQueueProvider;
    }

    @Override
    public void setOutboundQueueProvider(final OutboundQueueProvider outboundQueueProvider) {
        this.outboundQueueProvider = outboundQueueProvider;
    }

    @Override
    public CONNECTION_STATE getConnectionState() {
        return connectionState;
    }

    @Override
    public NodeId getNodeId() {
        return nodeId;
    }

    @Override
    public void setNodeId(final NodeId nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public void setDeviceDisconnectedHandler(final DeviceDisconnectedHandler deviceDisconnectedHandler) {
        this.deviceDisconnectedHandler = deviceDisconnectedHandler;
    }

    @Override
    public void closeConnection(boolean propagate) {
        LOG.debug("Actively closing connection: {}, nodeId:{}.",
                connectionAdapter.getRemoteAddress(), nodeId);
        connectionState = ConnectionContext.CONNECTION_STATE.CLOSED;

        unregisterOutboundQueue();
        if (getConnectionAdapter().isAlive()) {
            getConnectionAdapter().disconnect();
        }

        if (propagate) {
            propagateDeviceDisconnectedEvent();
        }
    }

    @Override
    public void onConnectionClosed() {
        connectionState = ConnectionContext.CONNECTION_STATE.CLOSED;

        final InetSocketAddress remoteAddress = connectionAdapter.getRemoteAddress();

        LOG.debug("disconnecting: node={}|connection state = {}",
                remoteAddress,
                getConnectionState());

        unregisterOutboundQueue();

        propagateDeviceDisconnectedEvent();
    }

    private void propagateDeviceDisconnectedEvent() {
        if (null != deviceDisconnectedHandler) {
            LOG.debug("Propagating connection closed event: {}, nodeId:{}.",
                    connectionAdapter.getRemoteAddress(), nodeId);
            deviceDisconnectedHandler.onDeviceDisconnected(this);
        }
    }

    @Override
    public void setOutboundQueueHandleRegistration(OutboundQueueHandlerRegistration<OutboundQueueProvider> outboundQueueHandlerRegistration) {
        this.outboundQueueHandlerRegistration = outboundQueueHandlerRegistration;
    }

    private void unregisterOutboundQueue() {
        if (outboundQueueHandlerRegistration != null) {
            outboundQueueHandlerRegistration.close();
            outboundQueueHandlerRegistration = null;
        }
    }

    @Override 
    public void setTlmTimeout(final XsdUnsignedShort tlmTimeout) {
        this.tlmTimeout = tlmTimeout;
    }

    @Override
    public XsdUnsignedShort getTlmTimeout() {
        return tlmTimeout;
    }

    @Override
    public void changeStateToHelloWait() {
        connectionState = CONNECTION_STATE.HELLO_WAIT;
    }

    @Override
    public void changeStateToEstablished() {
        connectionState = CONNECTION_STATE.ESTABLISHED;
    }

    @Override
    public void changeStateToMaintenance() {
        connectionState = CONNECTION_STATE.MAINTENANCE;
    }

}
