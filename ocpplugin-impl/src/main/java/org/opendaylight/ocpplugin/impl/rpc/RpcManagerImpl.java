/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.rpc;

import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceInitializationPhaseHandler;
import org.opendaylight.ocpplugin.api.ocp.rpc.RpcContext;
import org.opendaylight.ocpplugin.api.ocp.rpc.RpcManager;
import org.opendaylight.ocpplugin.impl.util.MdSalRegistratorUtils;

public class RpcManagerImpl implements RpcManager {

    private final RpcProviderRegistry rpcProviderRegistry;
    private DeviceInitializationPhaseHandler deviceInitPhaseHandler;
    private final int maxRequestsQuota;

    public RpcManagerImpl(final RpcProviderRegistry rpcProviderRegistry,
                          final int quotaValue) {
        this.rpcProviderRegistry = rpcProviderRegistry;
        maxRequestsQuota = quotaValue;
    }

    @Override
    public void setDeviceInitializationPhaseHandler(final DeviceInitializationPhaseHandler handler) {
        deviceInitPhaseHandler = handler;
    }

    @Override
    public void onDeviceContextLevelUp(final DeviceContext deviceContext) {
        final RpcContext rpcContext = new RpcContextImpl(rpcProviderRegistry, deviceContext, maxRequestsQuota);
        deviceContext.addDeviceContextClosedHandler(rpcContext);
        MdSalRegistratorUtils.registerServices(rpcContext, deviceContext);
        // finish device initialization cycle back to DeviceManager
        deviceInitPhaseHandler.onDeviceContextLevelUp(deviceContext);
    }
}
