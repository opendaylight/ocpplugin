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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates GetParamReq message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */


/* the rawMessage */
/* limitation: objId:1, param:1 */
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

                    //Retrieve single objId
                    seq.append("<obj objID=\"");
                    seq.append(message.getObjId().getValue().toString());
                    seq.append("\">");
                        //Retrieve value of paramName
                        seq.append("<param name=\"");
                        seq.append(message.getParamName().toString());
                        seq.append("\"/>");
                    seq.append("</obj>");

                seq.append("</"); seq.append(MESSAGE_TYPE); seq.append(">");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("GetParamInputFactory - composed xml-string = " + seq);
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }
}
