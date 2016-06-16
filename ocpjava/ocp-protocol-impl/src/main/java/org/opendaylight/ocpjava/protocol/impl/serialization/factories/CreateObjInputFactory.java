/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.serialization.factories;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPSerializer;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.createobjinput.Param;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates CreateObjReq message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/* limitation: objId:1, param:X */

/*
<!-- Example: Object Creation Request (multiple parameters, success) -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>8936</msgUID>
    </header>
    <body>
        <createObjReq>
            <objType objTypeID="exampleObj">
                <param name="parameter 1">value 1</param>
                <param name="parameter 2">value 2</param>
                <param name="parameter 3">value 3</param>
            </objType>
        </createObjReq>
    </body>
</msg>
*/

public class CreateObjInputFactory implements OCPSerializer<CreateObjInput> {

    /** Code type of CreateObjRequest message */
    private static final String MESSAGE_TYPE = "createObjReq";
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateObjInputFactory.class);

    @Override
    public void serialize(CreateObjInput message, ByteBuf outBuffer) {
        LOGGER.debug("CreateObjInputFactory - message = " + message.toString());
        StringBuilder seq = new StringBuilder("");
        //Generate from DTO to XML string
        seq.append("<msg xmlns=");
        seq.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq.append("<header>");
                seq.append("<msgType>REQ</msgType>");
                seq.append("<msgUID>"); seq.append(message.getXid().toString()); seq.append("</msgUID>");
            seq.append("</header>");
            seq.append("<body>");
                seq.append("<"); seq.append(MESSAGE_TYPE); seq.append(">");

                    //Retrieve single object Id
                    seq.append("<objType objTypeID=\"");
                    seq.append(message.getObjType().getValue());
                    seq.append("\">");
                    //Retrieve name and result of params
                    if (message.getParam() != null) {
                        for (Param currParam : message.getParam()) {
                            seq.append("<param name=\"");
                            seq.append(currParam.getName());
                            seq.append("\">");
                            seq.append(currParam.getValue());
                            seq.append("</param>");
                        }
                    }
                    seq.append("</objType>");

                seq.append("</"); seq.append(MESSAGE_TYPE); seq.append(">");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("CreateObjInputFactory - composed xml-string = " + seq);
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }
}
