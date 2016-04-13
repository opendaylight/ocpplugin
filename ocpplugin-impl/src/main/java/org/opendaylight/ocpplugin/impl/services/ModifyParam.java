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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yangtools.yang.binding.DataObject;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
final class ModifyParam extends AbstractSimpleService<ModifyParamInput, ModifyParamOutput> {

    ModifyParam(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        super(requestContextStack, deviceContext, ModifyParamOutput.class);
    }
    
    @Override
    protected OcpHeader buildRequest(final Xid xid, final ModifyParamInput input) {
        final ModifyParamInputBuilder inputBuilder = new ModifyParamInputBuilder();
        inputBuilder.setMsgType(OcpMsgType.MODIFYPARAMREQ);
        inputBuilder.setXid(xid.getValue());
        inputBuilder.setObj(input.getObj());
        return inputBuilder.build();
    }

}
