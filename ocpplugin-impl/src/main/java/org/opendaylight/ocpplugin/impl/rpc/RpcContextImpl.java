/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.rpc;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Semaphore;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RoutedRpcRegistration;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContext;
import org.opendaylight.ocpplugin.api.ocp.rpc.RpcContext;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContext;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcContextImpl implements RpcContext {
    private static final Logger LOG = LoggerFactory.getLogger(RpcContextImpl.class);
    private final RpcProviderRegistry rpcProviderRegistry;
    private final DeviceContext deviceContext;
    private final Semaphore tracker;

    // TODO: add private Sal salBroker
    private final Collection<RoutedRpcRegistration<?>> rpcRegistrations = new HashSet<>();

    public RpcContextImpl(final RpcProviderRegistry rpcProviderRegistry, final DeviceContext deviceContext, final int maxRequests) {
        this.rpcProviderRegistry = rpcProviderRegistry;
        this.deviceContext = Preconditions.checkNotNull(deviceContext);
        tracker = new Semaphore(maxRequests, true);
    }

    /**
     * @see org.opendaylight.ocpplugin.api.ocp.rpc.RpcContext#registerRpcServiceImplementation(java.lang.Class,
     * org.opendaylight.yangtools.yang.binding.RpcService)
     */
    @Override
    public <S extends RpcService> void registerRpcServiceImplementation(final Class<S> serviceClass,
                                                                        final S serviceInstance) {
        final RoutedRpcRegistration<S> routedRpcReg = rpcProviderRegistry.addRoutedRpcImplementation(serviceClass, serviceInstance);
        routedRpcReg.registerPath(NodeContext.class, deviceContext.getDeviceState().getNodeInstanceIdentifier());
        rpcRegistrations.add(routedRpcReg);
        LOG.debug("Registration of service {} for device {}.", serviceClass, deviceContext.getDeviceState().getNodeInstanceIdentifier());
    }

    /**
     * Unregisters all services.
     *
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        for (final RoutedRpcRegistration<?> rpcRegistration : rpcRegistrations) {
            rpcRegistration.unregisterPath(NodeContext.class, deviceContext.getDeviceState().getNodeInstanceIdentifier());
            rpcRegistration.close();
        }
    }

    @Override
    public <T> RequestContext<T> createRequestContext() {
        if (!tracker.tryAcquire()) {
            LOG.trace("Device queue {} at capacity", this);
            return null;
        }

        return new AbstractRequestContext<T>(deviceContext.getReservedXid()) {
            @Override
            public void close() {
                tracker.release();
                LOG.trace("Removed request context with xid {}", getXid().getValue());
            }
        };
    }

    @Override
    public void onDeviceContextClosed(DeviceContext deviceContext) {
        for (RoutedRpcRegistration<?> registration : rpcRegistrations) {
            registration.close();
        }
    }
}
