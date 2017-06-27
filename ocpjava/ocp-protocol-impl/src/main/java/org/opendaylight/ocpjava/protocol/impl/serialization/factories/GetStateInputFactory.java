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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates GetParamReq message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/* limitation: objId:1, stateType:1 */

/*
<!-- Example: Object State Reporting Request (wildcarded) -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>34561</msgUID>
    </header>
    <body>
        <getStateReq>
            <obj objID="exampleObj:0">
                <state type="ALL"/>
            </obj>
            <eventDrivenReporting>true</eventDrivenReporting>
        </getStateReq>
    </body>
</msg>
*/

public class GetStateInputFactory implements OCPSerializer<GetStateInput> {

    /** Code type of GetParamRequest message */
    private static final String MESSAGE_TYPE = "getStateReq";
    private static final Logger LOG = LoggerFactory.getLogger(GetStateInputFactory.class);

    @Override
    public void serialize(GetStateInput message, ByteBuf outBuffer) {
        LOG.debug("GetStateInputFactory - message = {}", message.toString());
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
                    seq.append("<obj objID=\"");
                    seq.append(message.getObjId().getValue().toString());
                    seq.append("\">");
                        //Retrieve single stateType
                        seq.append("<state type=\"");
                        seq.append(message.getStateType().toString());
                        seq.append("\"/>");
                    seq.append("</obj>");
                
                if(message.isEventDrivenReporting()){
                    seq.append("<eventDrivenReporting>true</eventDrivenReporting>");
                }
                else{
                    seq.append("<eventDrivenReporting>false</eventDrivenReporting>");
                }

                seq.append("</"); seq.append(MESSAGE_TYPE); seq.append(">");
            seq.append("</body>");
        seq.append("</msg>");

        LOG.debug("GetStateInputFactory - composed xml-string = {}", seq);
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }
}
