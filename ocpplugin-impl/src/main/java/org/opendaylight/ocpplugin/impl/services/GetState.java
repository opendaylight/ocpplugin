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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yangtools.yang.binding.DataObject;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
final class GetState extends AbstractSimpleService<GetStateInput, GetStateOutput> {

    GetState(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        super(requestContextStack, deviceContext, GetStateOutput.class);
    }
    
    @Override
    protected OcpHeader buildRequest(final Xid xid, final GetStateInput input) {
        final GetStateInputBuilder inputBuilder = new GetStateInputBuilder();
        inputBuilder.setMsgType(OcpMsgType.GETSTATEREQ);
        inputBuilder.setXid(xid.getValue());
        inputBuilder.setObjId(input.getObjId());
        inputBuilder.setStateType(input.getStateType());
        inputBuilder.setEventDrivenReporting(input.isEventDrivenReporting());
        return inputBuilder.build();
    }

}
