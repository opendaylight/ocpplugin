/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.rpc;

import org.opendaylight.ocpplugin.api.ocp.device.RequestContextStack;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceContextClosedHandler;
import org.opendaylight.yangtools.yang.binding.RpcService;

/*
 * This context is registered with MD-SAL as a routed RPC provider for the inventory node backed by this radio head and
 * tracks the state of any user requests and how they map onto protocol requests. It uses
 * org.opendaylight.ocpplugin.api.ocp.device.RequestContext to perform requests.
 */
public interface RpcContext extends RequestContextStack, AutoCloseable, DeviceContextClosedHandler {
    <S extends RpcService> void registerRpcServiceImplementation(Class<S> serviceClass, S serviceInstance);
}
