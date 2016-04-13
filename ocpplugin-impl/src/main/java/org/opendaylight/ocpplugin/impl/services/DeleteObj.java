/*
 * Copyright (c) 2015 Foxconn and Corporation others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.services;

import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContextStack;
import org.opendaylight.ocpplugin.api.ocp.device.Xid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yangtools.yang.binding.DataObject;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
final class DeleteObj extends AbstractSimpleService<DeleteObjInput, DeleteObjOutput> {

    DeleteObj(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        super(requestContextStack, deviceContext, DeleteObjOutput.class);
    }
    
    @Override
    protected OcpHeader buildRequest(final Xid xid, final DeleteObjInput input) {
        final DeleteObjInputBuilder inputBuilder = new DeleteObjInputBuilder();
        inputBuilder.setMsgType(OcpMsgType.DELETEOBJREQ);
        inputBuilder.setXid(xid.getValue());
        inputBuilder.setObjId(input.getObjId());
        return inputBuilder.build();
    }

}
