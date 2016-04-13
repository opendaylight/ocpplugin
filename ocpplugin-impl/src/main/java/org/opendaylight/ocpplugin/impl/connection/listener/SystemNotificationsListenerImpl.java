/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection.listener;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.device.Xid;
import org.opendaylight.ocpplugin.api.OcpConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SalDeviceMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.DisconnectEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.RadioHeadIdleEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.SystemNotificationsListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;
import org.opendaylight.controller.sal.binding.api.RpcConsumerRegistry;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.ocpplugin.impl.util.InventoryDataServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class SystemNotificationsListenerImpl implements SystemNotificationsListener {

    private static final Logger LOG = LoggerFactory.getLogger(SystemNotificationsListenerImpl.class);

    private final ConnectionContext connectionContext;
    private final SalDeviceMgmtService rpcService;

    public SystemNotificationsListenerImpl(@Nonnull final ConnectionContext connectionContext,
                                           @Nonnull final RpcConsumerRegistry rpcRegistry) {
        this.connectionContext = Preconditions.checkNotNull(connectionContext);
        this.rpcService = Preconditions.checkNotNull(rpcRegistry.getRpcService(SalDeviceMgmtService.class));
    }

    @Override
    public void onDisconnectEvent(final DisconnectEvent notification) {
        connectionContext.onConnectionClosed();
    }

    @Override
    public void onRadioHeadIdleEvent(final RadioHeadIdleEvent notification) {
 
       new Thread(new Runnable() {
            @Override
            public void run() {
                boolean shouldBeDisconnected = false;   /* FIXME: to disconnect or not to disconnect? */

                if (ConnectionContext.CONNECTION_STATE.ESTABLISHED.equals(connectionContext.getConnectionState()) ||
                    ConnectionContext.CONNECTION_STATE.MAINTENANCE.equals(connectionContext.getConnectionState())) {
                    LOG.debug("idle state occured, node={}  state={}",
                              connectionContext.getNodeId(), connectionContext.getConnectionState());

                    HealthCheckInputBuilder inputBuilder = new HealthCheckInputBuilder();  
                    inputBuilder.setNode(InventoryDataServiceUtil.nodeRefFromNodeKey(new NodeKey(connectionContext.getNodeId())));

                    try {
                        RpcResult<HealthCheckOutput> result = rpcService.healthCheck(inputBuilder.build()).get(OcpConstants.MAX_RPC_REPLY_TIMEOUT, TimeUnit.MILLISECONDS);
                        if (result.isSuccessful()) {
                            HealthCheckOutput output = result.getResult();
                            if (output.getResult() == OriRes.SUCCESS) {
                                LOG.debug("healthcheck succeeded");
                            }
                        } else {
                            LOG.warn("healthcheck failed");
                        }
                    } catch (Exception exc) {
                        LOG.warn("healthcheck failed. {}", exc);
                    }
                }

                if (shouldBeDisconnected) {
                    connectionContext.closeConnection(true);
                }

            }
        }).start();
    }

}
