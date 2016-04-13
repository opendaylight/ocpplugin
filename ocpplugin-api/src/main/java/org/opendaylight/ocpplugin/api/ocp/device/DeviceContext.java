/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.device;

import io.netty.util.Timeout;
import java.math.BigInteger;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceContextClosedHandler;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceDisconnectedHandler;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceReplyProcessor;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/*
 * The central entity of OCP is the Device Context, which encapsulate the logical state of a
 * radio head as seen by the controller. Each OCP session is tracked by a Connection Context.
 * These attach to a particular Device Context in such a way, that there is at most one primary
 * session associated with a Device Context. Whenever the controller needs to interact with a
 * particular radio head, it will do so in the context of the calling thread, obtaining a lock on
 * the corresponding Device Context – thus the Device Context becomes the fine-grained point
 * of synchronization. The only entity allowed to send requests towards the radio head is
 * RPC Manager. It allocates a Request Context for interacting
 * with a particular Device Context. The Request Contexts are the basic units of fairness,
 * which is enforced by keeping a cap on the number of outstanding requests a particular Request
 * Context can have at any point in time. Should this quota be exceeded, any further attempt to make
 * a request to the radio head will fail immediately, with proper error indication.
 */
public interface DeviceContext extends AutoCloseable,
        DeviceReplyProcessor,
        DeviceDisconnectedHandler {

    /**
     * Method provides state of device represented by this device context.
     *
     * @return DeviceState
     */
    DeviceState getDeviceState();

    /**
     * Method creates put operation using provided data in underlying transaction chain.
     */
    <T extends DataObject> void writeToTransaction(final LogicalDatastoreType store, final InstanceIdentifier<T> path, final T data);

    /**
     * Method creates delete operation for provided path in underlying transaction chain.
     */
    <T extends DataObject> void addDeleteToTxChain(final LogicalDatastoreType store, final InstanceIdentifier<T> path);

    /**
     * Method submits Transaction to DataStore.
     * @return transaction is submitted successfully
     */
    boolean submitTransaction();

    /**
     * Method exposes transaction created for device
     * represented by this context. This read only transaction has a fresh dataStore snapshot.
     * There is a possibility to get different data set from  DataStore
     * as write transaction in this context.
     */
    ReadTransaction getReadTransaction();

    /**
     * Method provides current devices connection context.
     *
     * @return
     */
    ConnectionContext getConnectionContext();

    /**
     * Sets notification service
     *
     * @param notificationService
     */
    void setNotificationService(NotificationService notificationService);

    void setNotificationPublishService(NotificationPublishService notificationPublishService);

    /**
     * Method sets reference to handler used for cleanup after device context about to be closed.
     */
    void addDeviceContextClosedHandler(DeviceContextClosedHandler deviceContextClosedHandler);

    Long getReservedXid();

    /**
     * indicates that device context is fully published
     */
    void onPublished();

}

