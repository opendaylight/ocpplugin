/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.services;

import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContextStack;
import org.opendaylight.ocpplugin.api.ocp.device.Xid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yangtools.yang.binding.DataObject;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
final class ModifyState extends AbstractSimpleService<ModifyStateInput, ModifyStateOutput> {

    ModifyState(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        super(requestContextStack, deviceContext, ModifyStateOutput.class);
    }
    
    @Override
    protected OcpHeader buildRequest(final Xid xid, final ModifyStateInput input) {
        final ModifyStateInputBuilder inputBuilder = new ModifyStateInputBuilder();
        inputBuilder.setMsgType(OcpMsgType.MODIFYSTATEREQ);
        inputBuilder.setXid(xid.getValue());
        inputBuilder.setObj(input.getObj());
        return inputBuilder.build();
    }

}
