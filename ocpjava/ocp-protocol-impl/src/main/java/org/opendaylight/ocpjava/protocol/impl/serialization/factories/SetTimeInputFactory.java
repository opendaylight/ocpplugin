/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.serialization.factories;

import io.netty.buffer.ByteBuf;
//import org.opendaylight.ocpjava.util.ByteBufUtils;
import io.netty.buffer.ByteBufUtil;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPSerializer;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;


/**
 * Translates HealthCheckRequest messages
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Set Time Request -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>1235</msgUID>
    </header>
    <body>
        <setTimeReq>
            <newTime>2012-04-26T10:23:00</newTime>
        </setTimeReq>
    </body>
</msg>

*/


public class SetTimeInputFactory implements OCPSerializer<SetTimeInput> {

    /** Code type of SetTimeRequest message */
    private static final String MESSAGE_TYPE = "setTimeReq";
    private static final Logger LOGGER = LoggerFactory.getLogger(SetTimeInputFactory.class);


    @Override
    public void serialize(SetTimeInput message, ByteBuf outBuffer) {
        LOGGER.debug("SetTimeInputFactory - message = " + message.toString());
        StringBuffer seq = new StringBuffer("");
        //Generate from DTO to XML string
        seq.append("<msg xmlns=");
        seq.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq.append("<header>");
                seq.append("<msgType>REQ</msgType>");
                seq.append("<msgUID>"); seq.append(message.getXid().toString()); seq.append("</msgUID>");
            seq.append("</header>");       
            seq.append("<body>");
                seq.append("<setTimeReq>");
                    seq.append("<newTime>"); 
                    seq.append(message.getNewTime().getValue().toString()); 
                    seq.append("</newTime>");
                seq.append("</setTimeReq>");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("SetTimeInputFactory - composed xml-string = " + seq);
        
        //CharSequence seq2 = seq;
        //write Xml string to ByteBuf by ByteBufUtil of netty-buffer
        ByteBufUtil.writeUtf8(outBuffer, seq);

    }
}
