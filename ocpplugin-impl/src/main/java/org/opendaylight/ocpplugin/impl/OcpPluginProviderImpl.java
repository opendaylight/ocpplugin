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
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.ocpjava.protocol.spi.connection.RadioHeadConnectionProvider;
import org.opendaylight.ocpplugin.api.ocp.OcpPluginProvider;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionManager;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceManager;
import org.opendaylight.ocpplugin.api.ocp.rpc.RpcManager;
import org.opendaylight.ocpplugin.api.OcpConstants;
import org.opendaylight.ocpplugin.impl.connection.ConnectionManagerImpl;
import org.opendaylight.ocpplugin.impl.device.DeviceManagerImpl;
import org.opendaylight.ocpplugin.impl.rpc.RpcManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class OcpPluginProviderImpl implements OcpPluginProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OcpPluginProviderImpl.class);

    private final String ocpVersion;
    private final int rpcRequestsQuota;
    private final long globalNotificationQuota;
    private DeviceManager deviceManager;
    private RpcManager rpcManager;
    private RpcProviderRegistry rpcProviderRegistry;
    private ConnectionManager connectionManager;
    private NotificationService notificationProviderService;
    private NotificationPublishService notificationPublishService;
    private DataBroker dataBroker;
    private Collection<RadioHeadConnectionProvider> radioHeadConnectionProviders;

    public OcpPluginProviderImpl(final String ocpVersion, final long rpcRequestsQuota,
                                 final Long globalNotificationQuota) {
        Preconditions.checkArgument(rpcRequestsQuota > 0 && rpcRequestsQuota <= Integer.MAX_VALUE, "rpcRequestQuota has to be in range <1,%s>", Integer.MAX_VALUE);
        this.ocpVersion = ocpVersion;
        this.rpcRequestsQuota = (int) rpcRequestsQuota;
        this.globalNotificationQuota = Preconditions.checkNotNull(globalNotificationQuota);
    }


    private void startRadioHeadConnections() {
        final List<ListenableFuture<Boolean>> starterChain = new ArrayList<>(radioHeadConnectionProviders.size());
        for (final RadioHeadConnectionProvider radioHeadConnectionPrv : radioHeadConnectionProviders) {
            radioHeadConnectionPrv.setRadioHeadConnectionHandler(connectionManager);
            final ListenableFuture<Boolean> isOnlineFuture = radioHeadConnectionPrv.startup();
            starterChain.add(isOnlineFuture);
        }

        final ListenableFuture<List<Boolean>> srvStarted = Futures.allAsList(starterChain);
        Futures.addCallback(srvStarted, new FutureCallback<List<Boolean>>() {
            @Override
            public void onSuccess(final List<Boolean> result) {
                LOG.info("All radioHeadConnectionProviders are up and running ({}).",
                        result.size());
            }

            @Override
            public void onFailure(final Throwable t) {
                LOG.warn("Some radioHeadConnectionProviders failed to start.", t);
            }
        });
    }

    @Override
    public void setRadioHeadConnectionProviders(final Collection<RadioHeadConnectionProvider> radioHeadConnectionProviders) {
        this.radioHeadConnectionProviders = radioHeadConnectionProviders;
    }

    @Override
    public void setDataBroker(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    @Override
    public void setRpcProviderRegistry(final RpcProviderRegistry rpcProviderRegistry) {
        this.rpcProviderRegistry = rpcProviderRegistry;
    }


    @Override
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

        startRadioHeadConnections();
    }

    @Override
    public void setNotificationProviderService(final NotificationService notificationProviderService) {
        this.notificationProviderService = notificationProviderService;
    }

    @Override
    public void setNotificationPublishService(final NotificationPublishService notificationPublishProviderService) {
        this.notificationPublishService = notificationPublishProviderService;
    }

    @Override
    public void close() throws Exception {
        //TODO: close all contexts, radioheadConnections (, managers)
    }
}
