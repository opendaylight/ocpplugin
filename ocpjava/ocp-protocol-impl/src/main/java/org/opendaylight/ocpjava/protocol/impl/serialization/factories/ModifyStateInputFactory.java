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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates ModifyStateReq message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/* limitation: objId:1, stateType:1, stateValue:1 */

/*
<!-- Example: Object State Modification Request -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>34570</msgUID>
    </header>
    <body>
        <modifyStateReq>
            <obj objID="exampleObj:0">
                <state type="AST">UNLOCKED</state>
            </obj>
        </modifyStateReq>
    </body>
</msg> 
*/

public class ModifyStateInputFactory implements OCPSerializer<ModifyStateInput> {

    /** Code type of ModifyStateRequest message */
    private static final String MESSAGE_TYPE = "modifyStateReq";
    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyStateInputFactory.class);

    @Override
    public void serialize(ModifyStateInput message, ByteBuf outBuffer) {
        LOGGER.debug("ModifyStateInputFactory - message = " + message.toString());
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
                        seq.append("\">");
                        //Retrieve single stateValue
                        //To fix YANG ENUM translation to Java code, it will remove the underline character
                        if(message.getStateValue().toString().equals("PREOPERATIONAL")){
                            seq.append("PRE_OPERATIONAL");
                        }
                        else if(message.getStateValue().toString().equals("NOTOPERATIONAL")){
                            seq.append("NOT_OPERATIONAL");
                        }
                        else{
                            seq.append(message.getStateValue());
                        }
                        seq.append("</state>");
                    seq.append("</obj>");

                seq.append("</"); seq.append(MESSAGE_TYPE); seq.append(">");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("ModifyStateInputFactory - composed xml-string = " + seq);
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }
}
