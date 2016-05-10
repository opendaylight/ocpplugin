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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.Param;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates GetParamRequest messages
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */


/* the rawMessage */
/*
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>6789</msgUID>
    </header>
    <body>
        <getParamReq>
            <obj objID="RE:0">
                <param name="vendorID"/>
            </obj>
        </getParamReq>
    </body>
</msg>
*/


public class GetParamInputFactory implements OCPSerializer<GetParamInput> {


    /** Code type of GetParamRequest message */
    private static final String MESSAGE_TYPE = "getParamReq";
    private static final Logger LOGGER = LoggerFactory.getLogger(GetParamInputFactory.class);

    @Override
    public void serialize(GetParamInput message, ByteBuf outBuffer) {

        LOGGER.debug("GetParamInputFactory - message = " + message.toString());
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
                //Retrival values from multiple objs
                for (Obj currObj : message.getObj()) {
                    seq.append("<obj objID=\"");
                    seq.append(currObj.getId().getValue().toString());
                    seq.append("\">");               
                    if(currObj.getParam() != null) {
                        for (Param currParam : currObj.getParam()) {
                           seq.append("<param name=\"");
                           seq.append(currParam.getName());
                           seq.append("\"/>");
                        }
                    }
                    seq.append("</obj>");
                }
                seq.append("</"); seq.append(MESSAGE_TYPE); seq.append(">");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("GetParamInputFactory - composed xml-string = " + seq);
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }
}
