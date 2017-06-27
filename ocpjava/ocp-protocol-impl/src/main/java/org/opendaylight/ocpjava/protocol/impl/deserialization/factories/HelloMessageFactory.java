/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessageBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.utils.MessageHelper;

import java.util.List;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Translates HelloInd message (OCP Protocol v4.1.1 Extension)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */


/*
<!-- Example: HelloInd Message from RRH to Controller -->
<?xml version="1.0" encoding="UTF-8"?>
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1"> 
        <header>
            <msgType>IND</msgType>
            <msgUID>0</msgUID>
        </header>
        <body>
            <helloInd>
                    <version>4.1.1</version>
                    <vendorId>XYZ</vendorId>
                    <serialNumber>ABC123</serialNumber>
            </helloInd>
        </body>
    </msg>
*/


public class HelloMessageFactory implements OCPDeserializer<HelloInd> {


	private static final Logger LOG = LoggerFactory.getLogger(HelloMessageFactory.class);

	@Override
    public HelloMessage deserialize(List<Object> rawMessage) {
        HelloMessageBuilder builder = new HelloMessageBuilder();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOG.trace("HelloMessageFactory - itr = {}", tok);
            try {
                if(tok instanceof XmlElementStart) {
                    //msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        String type = MessageHelper.getMsgType(itr);
                        builder.setMsgType(OcpMsgType.valueOf(type));
                    }
                    //msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        String uidStr = MessageHelper.getMsgUID(itr);
                        int uid = Integer.parseInt(uidStr);
                        builder.setXid((long)uid);
                    }
                    //version
                    else if (((XmlElementStart)tok).name().equals("version")){
                        String ver = MessageHelper.getCharVal(itr);
                        builder.setVersion(ver);
                    }
                    //versionId
                    else if (((XmlElementStart)tok).name().equals("vendorId")){
                        String verId = MessageHelper.getCharVal(itr);
                        builder.setVendorId(verId);
                    }
                    //serialNumber
                    else if (((XmlElementStart)tok).name().equals("serialNumber")){
                        String serNum = MessageHelper.getCharVal(itr);
                        builder.setSerialNumber(serNum);
                    }
                } 
            }
            catch( Exception t ) {
                 LOG.error("Error {} {}", tok, t.toString());
            }
        }
        LOG.debug("HelloMessageFactory - Builder: {}", builder.build());
        return builder.build();
    }
}
