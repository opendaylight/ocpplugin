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
//import org.opendaylight.ocpjava.util.ByteBufUtils;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateInput;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateinput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateinput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateinput.obj.State;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateinput.obj.StateBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Encoder test
import javax.xml.stream.XMLOutputFactory;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;


import javax.xml.stream.XMLStreamConstants;

/**
 * Translates GetParamRequest messages
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

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
        StringBuffer seq = new StringBuffer("");
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
                    for (State currState : currObj.getState()) {
                    	seq.append("<state type=\"");
                    	seq.append(currState.getName());
                    	seq.append("\">");

                    	// To fix YANG ENUM translation to Java code, it will remove the underline character 
                        if(currState.getValue().toString().equals("PREOPERATIONAL"))
                            seq.append("PRE_OPERATIONAL");
                        else if(currState.getValue().toString().equals("NOTOPERATIONAL"))
                            seq.append("NOT_OPERATIONAL");
                        else
                            seq.append(currState.getValue());

                    	seq.append("</state>");
                        }
                    seq.append("</obj>");
                    }
                seq.append("</"); seq.append(MESSAGE_TYPE); seq.append(">");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("ModifyStateInputFactory - composed xml-string = " + seq);
        
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }

}
