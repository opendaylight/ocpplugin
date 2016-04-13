/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.rpc;

import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceInitializationPhaseHandler;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceInitializator;

/*
 * The RPC Manager will maintain an RPC Context for each online radio head. RPC context for device is created when
 * org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceInitializationPhaseHandler#onDeviceContextLevelUp(org.opendaylight.ocpplugin.api.ocp.device.DeviceContext)
 * is called.
 */
public interface RpcManager extends DeviceInitializator, DeviceInitializationPhaseHandler {

}
