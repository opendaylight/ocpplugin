/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl;


import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.ocpjava.protocol.spi.connection.RadioHeadConnectionProvider;
import org.opendaylight.ocpplugin.api.OcpConstants;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionManager;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceManager;
import org.opendaylight.ocpplugin.api.ocp.rpc.RpcManager;
import org.opendaylight.ocpplugin.impl.connection.ConnectionManagerImpl;
import org.opendaylight.ocpplugin.impl.device.DeviceManagerImpl;
import org.opendaylight.ocpplugin.impl.rpc.RpcManagerImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.plugin.config.rev170807.OcpPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class OcpPluginProviderImpl implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(OcpPluginProviderImpl.class);

    private final String ocpVersion;
    private final int rpcRequestsQuota;
    private final long globalNotificationQuota;
    private DeviceManager deviceManager;
    private RpcManager rpcManager;
    private final RpcProviderRegistry rpcProviderRegistry;
    private ConnectionManager connectionManager;
    private final NotificationService notificationProviderService;
    private final NotificationPublishService notificationPublishService;
    private final DataBroker dataBroker;

    public OcpPluginProviderImpl(final OcpPluginConfig config, final DataBroker dataBroker,
            final RpcProviderRegistry rpcProviderRegistry, final NotificationService notificationProviderService,
            final NotificationPublishService notificationPublishService) {
        this.rpcRequestsQuota = Preconditions.checkNotNull(config.getRpcRequestsQuota()).intValue();
        Preconditions.checkArgument(rpcRequestsQuota > 0 && rpcRequestsQuota <= Integer.MAX_VALUE,
                "rpcRequestQuota has to be in range <1,%s>", Integer.MAX_VALUE);
        this.ocpVersion = Preconditions.checkNotNull(config.getOcpVersion());
        this.globalNotificationQuota = Preconditions.checkNotNull(config.getGlobalNotificationQuota());
        this.dataBroker = dataBroker;
        this.rpcProviderRegistry = rpcProviderRegistry;
        this.notificationProviderService = notificationProviderService;
        this.notificationPublishService = notificationPublishService;
    }

    public void radioHeadConnectionProviderAdded(RadioHeadConnectionProvider provider) {
        LOG.info("RadioHeadConnectionProvider {} added", provider);

        provider.setRadioHeadConnectionHandler(connectionManager);

        final ListenableFuture<Boolean> isOnlineFuture = provider.startup();
        Futures.addCallback(isOnlineFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                LOG.info("RadioHeadConnectionProvider {} is up and running", provider);
            }

            @Override
            public void onFailure(Throwable ex) {
                LOG.warn("RadioHeadConnectionProvider {} failed to start", provider, ex);
            }
        }, MoreExecutors.directExecutor());
    }

    public void radioHeadConnectionProviderRemoved(RadioHeadConnectionProvider provider) {
    }

    public void initialize() {

        Preconditions.checkNotNull(dataBroker, "missing data broker");
        Preconditions.checkNotNull(rpcProviderRegistry, "missing RPC provider registry");
        Preconditions.checkNotNull(notificationProviderService, "missing notification provider service");

        connectionManager = new ConnectionManagerImpl(rpcProviderRegistry);
        deviceManager = new DeviceManagerImpl(dataBroker, globalNotificationQuota);
        rpcManager = new RpcManagerImpl(rpcProviderRegistry, rpcRequestsQuota);

        OcpConstants.OCP_VERSION = ocpVersion;

        connectionManager.setDeviceConnectedHandler(deviceManager);
        deviceManager.setDeviceInitializationPhaseHandler(rpcManager);
        deviceManager.setNotificationService(this.notificationProviderService);
        deviceManager.setNotificationPublishService(this.notificationPublishService);
        rpcManager.setDeviceInitializationPhaseHandler(deviceManager);

        deviceManager.initialize();

        LOG.info("OcpPluginProviderImpl initialized - ocpVersion: {}, rpcRequestsQuota: {}, globalNotificationQuota: {}",
                ocpVersion, rpcRequestsQuota, globalNotificationQuota);
    }

    @Override
    public void close() {
        //TODO: close all contexts, radioheadConnections (, managers)
    }
}
