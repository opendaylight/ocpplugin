/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.device;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueue;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueueHandlerRegistration;
import org.opendaylight.ocpplugin.api.ConnectionException;
import org.opendaylight.ocpplugin.api.OcpConstants;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.OutboundQueueProvider;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceManager;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceState;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContext;
import org.opendaylight.ocpplugin.api.ocp.device.Xid;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceInitializationPhaseHandler;
import org.opendaylight.ocpplugin.impl.connection.OutboundQueueProviderImpl;
import org.opendaylight.ocpplugin.impl.device.listener.OcpProtocolListenerFullImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnectorBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Ipv4Address;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnectedBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceDisconnectedBuilder;
import org.opendaylight.ocpplugin.impl.rpc.AbstractRequestContext;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.opendaylight.ocpplugin.impl.util.InventoryDataServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class DeviceManagerImpl implements DeviceManager, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceManagerImpl.class);

    private final long globalNotificationQuota;
    private final DataBroker dataBroker;
    private DeviceInitializationPhaseHandler deviceInitPhaseHandler;
    private NotificationService notificationService;
    private NotificationPublishService notificationPublishService;

    private final Set<DeviceContext> deviceContexts = Sets.newConcurrentHashSet();
    private final int maxQueueDepth = 25600;
    private final DeviceTransactionChainManagerProvider deviceTransactionChainManagerProvider;

    public DeviceManagerImpl(@Nonnull final DataBroker dataBroker,
                             final long globalNotificationQuota) {
        this.globalNotificationQuota = globalNotificationQuota;
        this.dataBroker = Preconditions.checkNotNull(dataBroker);
        /* merge empty nodes to oper DS to predict any problems with missing parent for Node */
        final WriteTransaction tx = dataBroker.newWriteOnlyTransaction();

        final NodesBuilder nodesBuilder = new NodesBuilder();
        nodesBuilder.setNode(Collections.<Node>emptyList());
        tx.merge(LogicalDatastoreType.OPERATIONAL, InstanceIdentifier.create(Nodes.class), nodesBuilder.build());
        try {
            tx.submit().get();
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("Creation of node failed.", e);
            throw new IllegalStateException(e);
        }
        deviceTransactionChainManagerProvider = new DeviceTransactionChainManagerProvider(dataBroker);
    }


    @Override
    public void setDeviceInitializationPhaseHandler(final DeviceInitializationPhaseHandler handler) {
        deviceInitPhaseHandler = handler;
    }

    @Override
    public void onDeviceContextLevelUp(final DeviceContext deviceContext) {
        // final phase - we have to add new Device to MD-SAL DataStore
        Preconditions.checkNotNull(deviceContext);
        try {
            ((DeviceContextImpl) deviceContext).initialSubmitTransaction();
            deviceContext.onPublished();
        } catch (final Exception e) {
            LOG.warn("Node {} cannot be added to OPERATIONAL DataStore yet because {} ", deviceContext.getDeviceState().getNodeId(), e.getMessage());
            LOG.trace("Problem with adding node {} to OPERATIONAL DataStore", deviceContext.getDeviceState().getNodeId(), e);
            try {
                deviceContext.close();
            } catch (final Exception e1) {
                LOG.warn("Device context close FAIL - " + deviceContext.getDeviceState().getNodeId());
            }
        }
    }

    @Override
    public void deviceConnected(@CheckForNull final ConnectionContext connectionContext) {
        Preconditions.checkArgument(connectionContext != null);

        ReadyForNewTransactionChainHandler readyForNewTransactionChainHandler = new ReadyForNewTransactionChainHandlerImpl(this, connectionContext);
        DeviceTransactionChainManagerProvider.TransactionChainManagerRegistration transactionChainManagerRegistration = deviceTransactionChainManagerProvider.provideTransactionChainManager(connectionContext);
        TransactionChainManager transactionChainManager = transactionChainManagerRegistration.getTransactionChainManager();

        //this actually is new registration for currently processed connection context
        if (transactionChainManagerRegistration.ownedByInvokingConnectionContext()) {
            initializeDeviceContext(connectionContext, transactionChainManager);
        }
        //this means there already exists connection described by same NodeId and it is not current connection contexts' registration
        else if (TransactionChainManager.TransactionChainManagerStatus.WORKING.equals(transactionChainManager.getTransactionChainManagerStatus())) {
            connectionContext.closeConnection(false);
        }
        //previous connection is shutting down, we will try to register handler listening on new transaction chain ready
        else if (!transactionChainManager.attemptToRegisterHandler(readyForNewTransactionChainHandler)) {
            // new connection wil be closed if handler registration fails
            connectionContext.closeConnection(false);
        }

        // notify the ocp service to initiate alignment operations
        DeviceConnectedBuilder builder = new DeviceConnectedBuilder();
        builder.setNodeId(connectionContext.getNodeId());
        builder.setReIpAddr(new Ipv4Address(connectionContext.getConnectionAdapter().getRemoteAddress().getAddress().toString().replace("/", "")));

        final ListenableFuture<? extends Object> offerNotification = notificationPublishService.offerNotification(builder.build());
        if (NotificationPublishService.REJECTED.equals(offerNotification)) {
            LOG.debug("notification offer rejected");
            return;
        }

        Futures.addCallback(offerNotification, new FutureCallback<Object>() {
            @Override
            public void onSuccess(final Object result) {
                LOG.trace("notification offer success..", result);
            }

            @Override
            public void onFailure(final Throwable t) {
                LOG.debug("notification offer failed: {}", t.getMessage());
                LOG.trace("notification offer failed..", t);
            }
        });
    }

    private void initializeDeviceContext(final ConnectionContext connectionContext, final TransactionChainManager transactionChainManager) {
        final ConnectionAdapter connectionAdapter = connectionContext.getConnectionAdapter();

        final OutboundQueueProvider outboundQueueProvider = new OutboundQueueProviderImpl();

        connectionContext.setOutboundQueueProvider(outboundQueueProvider);
        final OutboundQueueHandlerRegistration<OutboundQueueProvider> outboundQueueHandlerRegistration =
                connectionAdapter.registerOutboundQueueHandler(outboundQueueProvider, maxQueueDepth);
        connectionContext.setOutboundQueueHandleRegistration(outboundQueueHandlerRegistration);

        final NodeId nodeId = connectionContext.getNodeId();
        final DeviceState deviceState = new DeviceStateImpl(nodeId);

        final DeviceContext deviceContext = new DeviceContextImpl(connectionContext, deviceState, dataBroker,
                  outboundQueueProvider, transactionChainManager);
        deviceContext.setNotificationService(notificationService);
        deviceContext.setNotificationPublishService(notificationPublishService);
        final NodeBuilder nodeBuilder = new NodeBuilder().setId(deviceState.getNodeId()).setNodeConnector(Collections.<NodeConnector>emptyList());
        try {
            deviceContext.writeToTransaction(LogicalDatastoreType.OPERATIONAL, deviceState.getNodeInstanceIdentifier(), nodeBuilder.build());
        } catch (final Exception e) {
            LOG.debug("Failed to write node to DS ", e);
        }

        connectionContext.setDeviceDisconnectedHandler(deviceContext);
        deviceContext.addDeviceContextClosedHandler(this);
        deviceContexts.add(deviceContext);

        final OcpProtocolListenerFullImpl messageListener = new OcpProtocolListenerFullImpl(connectionAdapter, deviceContext);
        connectionAdapter.setMessageListener(messageListener);
        connectionAdapter.setMessageExtListener(messageListener);

        deviceCtxLevelUp(deviceContext);
    }

    private void deviceCtxLevelUp(final DeviceContext deviceContext) {
        deviceContext.getDeviceState().setValid(true);
        deviceInitPhaseHandler.onDeviceContextLevelUp(deviceContext);
        LOG.trace("Device context level up called.");
    }

    @Override
    public void setNotificationService(final NotificationService notificationServiceParam) {
        notificationService = notificationServiceParam;
    }

    @Override
    public void setNotificationPublishService(final NotificationPublishService notificationService) {
        notificationPublishService = notificationService;
    }

    @Override
    public void close() throws Exception {
        for (final DeviceContext deviceContext : deviceContexts) {
            deviceContext.close();
        }
    }

    @Override
    public void onDeviceContextClosed(final DeviceContext deviceContext) {
        deviceContexts.remove(deviceContext);
 
        DeviceDisconnectedBuilder builder = new DeviceDisconnectedBuilder();
        builder.setNodeId(deviceContext.getConnectionContext().getNodeId());

        final ListenableFuture<? extends Object> offerNotification = notificationPublishService.offerNotification(builder.build());
        if (NotificationPublishService.REJECTED.equals(offerNotification)) {
            LOG.debug("notification offer rejected");
            return;
        }

        Futures.addCallback(offerNotification, new FutureCallback<Object>() {
            @Override
            public void onSuccess(final Object result) {
                LOG.trace("notification offer success..", result);
            }

            @Override
            public void onFailure(final Throwable t) {
                LOG.debug("notification offer failed: {}", t.getMessage());
                LOG.trace("notification offer failed..", t);
            }
        });
    }

    @Override
    public void initialize() {
    }

}
