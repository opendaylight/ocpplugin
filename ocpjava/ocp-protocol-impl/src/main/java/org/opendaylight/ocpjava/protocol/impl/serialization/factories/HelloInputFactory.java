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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates Hello Input messages
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example1: Hello Request(No repsponse) -->
<?xml version="1.0" encoding="UTF-8"?>
    <msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://uri.etsi.org/ori/002-2/v4.1.1 ORI_Messages_v4.1.1.xsd">
        <header>
            <msgType>ACK</msgType>
            <msgUID>0</msgUID>
        </header>
        <body>
            <helloAck>
                    <result>FAIL_OCP_VERSION</result>
            </helloAck>
        </body>
    </msg>
*/


public class HelloInputFactory implements OCPSerializer<HelloInput>{

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloInputFactory.class);

    @Override
    public void serialize(HelloInput message, ByteBuf outBuffer) {
        LOGGER.debug("HelloInputFactory - message = " + message.toString());
        StringBuilder seq = new StringBuilder("");
        //Generate from DTO to XML string
        seq.append("<msg xmlns=");
        seq.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq.append("<header>");
                seq.append("<msgType>ACK</msgType>");
                seq.append("<msgUID>"); seq.append(message.getXid()); seq.append("</msgUID>");
            seq.append("</header>");       
            seq.append("<body>");
                seq.append("<helloAck>");
                    seq.append("<result>"); seq.append(message.getResult().toString()); seq.append("</result>");
                seq.append("</helloAck>");
            seq.append("</body>");
        seq.append("</msg>");

        LOGGER.debug("HelloInputFactory - composed xml-string = " + seq);
        
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }

}
