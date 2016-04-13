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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Encoder test
import javax.xml.stream.XMLOutputFactory;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;



/**
 * Translates HealthCheckRequest messages
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: ReReset Request -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>2345</msgUID>
    </header>
    <body>
        <resetReq/>
    </body>
</msg>
*/

public class ReResetInputFactory implements OCPSerializer<ReResetInput> {

    /** Code type of HealthCheckRequest message */
    private static final String MESSAGE_TYPE = "resetReq";
	private static final Logger LOGGER = LoggerFactory.getLogger(ReResetInputFactory.class);

    @Override
    public void serialize(ReResetInput message, ByteBuf outBuffer) {
        LOGGER.debug("ReResetInputFactory - message = " + message.toString());    	
        StringBuffer seq = new StringBuffer("");
        //Generate from DTO to XML string
        seq.append("<msg xmlns=");
        seq.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq.append("<header>");
                seq.append("<msgType>REQ</msgType>");
                seq.append("<msgUID>"); seq.append(message.getXid().toString()); seq.append("</msgUID>");
            seq.append("</header>");       
            seq.append("<body>");
                seq.append("<resetReq/>");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("ReResetInputFactory - composed xml-string = " + seq);

        //CharSequence seq2 = seq;
        //write Xml string to ByteBuf by ByteBufUtil of netty-buffer
        ByteBufUtil.writeUtf8(outBuffer, seq);

    }
}
