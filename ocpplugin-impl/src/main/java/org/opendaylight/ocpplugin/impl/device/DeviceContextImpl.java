/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.device;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueue;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.OutboundQueueProvider;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceState;
import org.opendaylight.ocpplugin.api.ocp.device.Xid;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceContextClosedHandler;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.Notification;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChange;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultIndBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeBuilder; 
import org.opendaylight.ocpplugin.impl.util.InventoryDataServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class DeviceContextImpl implements DeviceContext {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceContextImpl.class);

    private final ConnectionContext connectionContext;
    private final DeviceState deviceState;
    private final DataBroker dataBroker;
    private final TransactionChainManager transactionChainManager;
    private final Collection<DeviceContextClosedHandler> closeHandlers = new HashSet<>();
    private NotificationPublishService notificationPublishService;
    private NotificationService notificationService;
    private final OutboundQueue outboundQueueProvider;

    @VisibleForTesting
    DeviceContextImpl(@Nonnull final ConnectionContext connectionContext,
                      @Nonnull final DeviceState deviceState,
                      @Nonnull final DataBroker dataBroker,
                      @Nonnull final OutboundQueueProvider outboundQueueProvider,
                      @Nonnull final TransactionChainManager transactionChainManager) {
        this.connectionContext = Preconditions.checkNotNull(connectionContext);
        this.deviceState = Preconditions.checkNotNull(deviceState);
        this.dataBroker = Preconditions.checkNotNull(dataBroker);
        this.outboundQueueProvider = Preconditions.checkNotNull(outboundQueueProvider);
        this.transactionChainManager = Preconditions.checkNotNull(transactionChainManager);
    }

    /**
     * This method is called from {@link DeviceManagerImpl} only. So we could say "posthandshake process finish"
     * and we are able to set a scheduler for an automatic transaction submitting by time (0,5sec).
     */
    void initialSubmitTransaction() {
        transactionChainManager.initialSubmitWriteTransaction();
    }

    @Override
    public Long getReservedXid() {
        return outboundQueueProvider.reserveEntry();
    }

    @Override
    public DeviceState getDeviceState() {
        return deviceState;
    }

    @Override
    public ReadTransaction getReadTransaction() {
        return dataBroker.newReadOnlyTransaction();
    }

    @Override
    public <T extends DataObject> void writeToTransaction(final LogicalDatastoreType store,
                                                          final InstanceIdentifier<T> path, final T data) {
        transactionChainManager.writeToTransaction(store, path, data);
    }

    @Override
    public <T extends DataObject> void addDeleteToTxChain(final LogicalDatastoreType store, final InstanceIdentifier<T> path) {
        transactionChainManager.addDeleteOperationTotTxChain(store, path);
    }

    @Override
    public boolean submitTransaction() {
        return transactionChainManager.submitWriteTransaction();
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    @Override
    public void close() {
        LOG.debug("closing deviceContext: {}, nodeId:{}",
                getConnectionContext().getConnectionAdapter().getRemoteAddress(),
                getDeviceState().getNodeId());

        tearDown();

        connectionContext.closeConnection(false);
    }

    private void tearDown() {
        deviceState.setValid(false);

        for (final DeviceContextClosedHandler deviceContextClosedHandler : closeHandlers) {
            deviceContextClosedHandler.onDeviceContextClosed(this);
        }

        transactionChainManager.close();
    }

    @Override
    public void onDeviceDisconnected(final ConnectionContext connectionContext) {
        if (getConnectionContext().equals(connectionContext)) {
            try {
                tearDown();
            } catch (final Exception e) {
                LOG.trace("Error closing device context.");
            }
        } else {
            LOG.debug("auxiliary connection dropped: {}, nodeId:{}",
                    connectionContext.getConnectionAdapter().getRemoteAddress(),
                    getDeviceState().getNodeId());
        }
    }

    @Override
    public void setNotificationService(final NotificationService notificationServiceParam) {
        notificationService = notificationServiceParam;
    }

    @Override
    public void setNotificationPublishService(final NotificationPublishService notificationPublishService) {
        this.notificationPublishService = notificationPublishService;
    }

    @Override
    public void addDeviceContextClosedHandler(final DeviceContextClosedHandler deviceContextClosedHandler) {
        closeHandlers.add(deviceContextClosedHandler);
    }

    @Override
    public void onPublished() {
    }

    private void publishNotification(Notification notification) {
        final ListenableFuture<? extends Object> offerNotification = notificationPublishService.offerNotification(notification);
        if (NotificationPublishService.REJECTED.equals(offerNotification)) {
            LOG.debug("notification offer rejected");
            return;
        }

        Futures.addCallback(offerNotification, new FutureCallback<Object>() {
            @Override
            public void onSuccess(final Object result) {
            }

            @Override
            public void onFailure(final Throwable t) {
                LOG.debug("notification offer failed: {}", t.getMessage());
                LOG.trace("notification offer failed..", t);
            }
        });
    } 

    @Override
    public void processFaultIndication(FaultInd faultInd) {
        FaultIndBuilder builder = new FaultIndBuilder();
        builder.setNode(InventoryDataServiceUtil.nodeRefFromNodeKey(new NodeKey(connectionContext.getNodeId())));
        builder.setObj(faultInd.getObj());        
        publishNotification(builder.build()); 
    }

    @Override
    public void processStateChange(StateChange stateChange) {
        StateChangeBuilder builder = new StateChangeBuilder();
        builder.setNode(InventoryDataServiceUtil.nodeRefFromNodeKey(new NodeKey(connectionContext.getNodeId())));
        builder.setObj(stateChange.getObj());
        publishNotification(builder.build());
    }

}
