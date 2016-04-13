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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yangtools.yang.binding.DataObject;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
final class CreateObj extends AbstractSimpleService<CreateObjInput, CreateObjOutput> {

    CreateObj(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        super(requestContextStack, deviceContext, CreateObjOutput.class);
    }
    
    @Override
    protected OcpHeader buildRequest(final Xid xid, final CreateObjInput input) {
        final CreateObjInputBuilder inputBuilder = new CreateObjInputBuilder();
        inputBuilder.setMsgType(OcpMsgType.CREATEOBJREQ);
        inputBuilder.setXid(xid.getValue());
        inputBuilder.setObj(input.getObj());
        return inputBuilder.build();
    }

}
