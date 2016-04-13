/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.connection;

import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueue;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueueHandlerRegistration;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceDisconnectedHandler;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;

/*
 * Each OCP session is tracked by a Connection Context. These attach to a particular Device Context in such a way,
 * that there is at most one primary session associated with a Device Context.
 * 
 * @author Richard Chien <richard.chien@foxconn.com>
 */
public interface ConnectionContext {

    /**
     * distinguished connection states
     */
    enum CONNECTION_STATE {
        /**
         * initial phase of talking to radio head
         */
        HELLO_WAIT,
        /**
         * standard phase - interacting with radio head
         */
        ESTABLISHED,
        /**
         * maintenance phase - OCP version mismatch
         */
        MAINTENANCE,
        /**
         * talking to radio head is over
         */
        CLOSED
    }

    /**
     * setter for nodeId
     *
     * @param nodeId
     */
    void setNodeId(NodeId nodeId);

    /**
     * Method returns identifier of device whic connection represents this context.
     *
     * @return {@link org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId}
     */
    NodeId getNodeId();

    /**
     * @return the connectionAdapter
     */
    ConnectionAdapter getConnectionAdapter();

    /**
     * Returns reference to OCPJava outbound queue provider. Outbound queue is used for outbound messages processing.
     *
     * @return
     */
    OutboundQueue getOutboundQueueProvider();

    /**
     * Method sets reference to OCPJava outbound queue provider.
     */
    void setOutboundQueueProvider(OutboundQueueProvider outboundQueueProvider);

    /**
     * setter for TLM timeout
     *
     * @param tlmTimeout
     */
    void setTlmTimeout(XsdUnsignedShort tlmTimeout);

    /**
     * Method returns TLM timeout.
     *
     * @return tlmTimeout
     */
    XsdUnsignedShort getTlmTimeout();

    /**
     * Method returns current connection state.
     *
     * @return {@link ConnectionContext.CONNECTION_STATE}
     */
    CONNECTION_STATE getConnectionState();

    /**
     * Method sets handler for handling closing connections.
     *
     * @param deviceDisconnectedHandler
     */
    void setDeviceDisconnectedHandler(DeviceDisconnectedHandler deviceDisconnectedHandler);

    void setOutboundQueueHandleRegistration(OutboundQueueHandlerRegistration<OutboundQueueProvider> outboundQueueHandlerRegistration);

    /**
     * actively drop associated connection
     *
     * @param propagate true if event need to be propagated to higher contexts (device, stats, rpc..)
     *                  or false if invoked from higher context
     * @see ConnectionAdapter#disconnect()
     */
    void closeConnection(boolean propagate);

    /**
     * cleanup context upon connection closed event (by device)
     */
    void onConnectionClosed();

    /**
     * change internal state to {@link ConnectionContext.CONNECTION_STATE#HELLO_WAIT}
     */
    void changeStateToHelloWait();

    /**
     * change internal state to {@link ConnectionContext.CONNECTION_STATE#ESTABLISHED}
     */
    void changeStateToEstablished();

    /**
     * change internal state to {@link ConnectionContext.CONNECTION_STATE#MAINTENANCE}
     */
    void changeStateToMaintenance();

}
