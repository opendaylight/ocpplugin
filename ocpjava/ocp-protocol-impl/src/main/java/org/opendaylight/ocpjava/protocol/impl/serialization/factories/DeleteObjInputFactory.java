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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates DeleteObjRequest messages
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Object Deletion Request -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>1234</msgUID>
    </header>
    <body>
        <deleteObjReq>
            <obj objID="exampleObj:0"/>
        </deleteObjReq>
    </body>
</msg>

*/

public class DeleteObjInputFactory implements OCPSerializer<DeleteObjInput> {

    /** Code type of DeleteObjRequest message */
    private static final String MESSAGE_TYPE = "deleteObjReq";
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteObjInputFactory.class);

    @Override
    public void serialize(DeleteObjInput message, ByteBuf outBuffer) {
        LOGGER.debug("DeleteObjInputFactory - message = " + message.toString());
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
                    seq.append("<obj objID=\""); 
                    seq.append(message.getObjId().getValue().toString()); 
                    seq.append("\"/>");
                seq.append("</"); seq.append(MESSAGE_TYPE); seq.append(">");
            seq.append("</body>");
            seq.append("</msg>");

        LOGGER.debug("DeleteObjInputFactory - composed xml-string = " + seq);
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }
}
