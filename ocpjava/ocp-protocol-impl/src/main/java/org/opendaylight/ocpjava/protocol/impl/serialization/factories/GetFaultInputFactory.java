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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getfaultinput.Obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates GetParamReq message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Fault Reporting Request -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>34650</msgUID>
    </header>
    <body>
        <getFaultReq>
            <obj objID="ALL"/>
            <eventDrivenReporting>true</eventDrivenReporting>
        </getFaultReq>
    </body>
</msg>
 */

public class GetFaultInputFactory implements OCPSerializer<GetFaultInput> {

    /** Code type of GetFaultInput message */
    private static final String MESSAGE_TYPE = "getFaultReq";
    private static final Logger LOGGER = LoggerFactory.getLogger(GetFaultInputFactory.class);

    @Override
    public void serialize(GetFaultInput message, ByteBuf outBuffer) {
        LOGGER.debug("GetFaultInputFactory - message = " + message.toString());
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
                    seq.append("\"/>");               
                }
                
                if(message.isEventDrivenReporting()){
                    seq.append("<eventDrivenReporting>true</eventDrivenReporting>");
                }
                else{
                    seq.append("<eventDrivenReporting>false</eventDrivenReporting>");
                }
                seq.append("</"); seq.append(MESSAGE_TYPE); seq.append(">");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("GetFaultInputFactory - composed xml-string = " + seq);
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }
}
