/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.util;

import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.rpc.RpcContext;
import org.opendaylight.ocpplugin.impl.services.SalDeviceMgmtServiceImpl;
import org.opendaylight.ocpplugin.impl.services.SalConfigMgmtServiceImpl;
import org.opendaylight.ocpplugin.impl.services.SalObjectLifecycleServiceImpl;
import org.opendaylight.ocpplugin.impl.services.SalObjectStateMgmtServiceImpl;
import org.opendaylight.ocpplugin.impl.services.SalFaultMgmtServiceImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SalDeviceMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.SalConfigMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.SalObjectLifecycleService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.SalObjectStateMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.SalFaultMgmtService;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class MdSalRegistratorUtils {

    private MdSalRegistratorUtils() {
        throw new IllegalStateException();
    }

    public static void registerServices(final RpcContext rpcContext, final DeviceContext deviceContext) {
        rpcContext.registerRpcServiceImplementation(SalDeviceMgmtService.class, new SalDeviceMgmtServiceImpl(rpcContext, deviceContext));
        rpcContext.registerRpcServiceImplementation(SalConfigMgmtService.class, new SalConfigMgmtServiceImpl(rpcContext, deviceContext)); 
rpcContext.registerRpcServiceImplementation(SalObjectLifecycleService.class, new SalObjectLifecycleServiceImpl(rpcContext, deviceContext));
        rpcContext.registerRpcServiceImplementation(SalObjectStateMgmtService.class, new SalObjectStateMgmtServiceImpl(rpcContext, deviceContext));
        rpcContext.registerRpcServiceImplementation(SalFaultMgmtService.class, new SalFaultMgmtServiceImpl(rpcContext, deviceContext)); 
   }

}
