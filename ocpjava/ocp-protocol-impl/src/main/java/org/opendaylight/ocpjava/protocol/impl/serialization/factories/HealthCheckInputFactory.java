/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.serialization.factories;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.*;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPSerializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates CreateObjReq message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Health Check Request -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>REQ</msgType>
        <msgUID>1234</msgUID>
    </header>
    <body>
        <healthCheckReq>
            <tcpLinkMonTimeout>255</tcpLinkMonTimeout>
        </healthCheckReq>
    </body>
</msg>
*/

public class HealthCheckInputFactory implements OCPSerializer<HealthCheckInput> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckInputFactory.class);

    @Override
    public void serialize(HealthCheckInput message, ByteBuf outBuffer) {
        LOGGER.debug("HealthCheckInputFactory - message = " + message.toString());
        StringBuilder seq = new StringBuilder("");
        //Generate from DTO to XML string
        seq.append("<msg xmlns=");
        seq.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq.append("<header>");
                seq.append("<msgType>REQ</msgType>");
                seq.append("<msgUID>"); seq.append(message.getXid().toString()); seq.append("</msgUID>");
            seq.append("</header>");       
            seq.append("<body>");
                seq.append("<healthCheckReq>");
                    seq.append("<tcpLinkMonTimeout>"); 
                    seq.append(message.getTcpLinkMonTimeout().getValue().toString()); 
                    seq.append("</tcpLinkMonTimeout>");
                seq.append("</healthCheckReq>");
            seq.append("</body>");
            seq.append("</msg>");

        LOGGER.debug("HealthCheckInputFactory - composed xml-string = " + seq);    
            
        //write Xml string to ByteBuf by ByteBufUtil of netty-buffer
        ByteBufUtil.writeUtf8(outBuffer, seq);
    }
}
