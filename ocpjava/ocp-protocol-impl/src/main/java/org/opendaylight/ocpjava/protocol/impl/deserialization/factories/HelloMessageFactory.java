/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import io.netty.buffer.ByteBuf;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessageBuilder;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInd;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Translates HelloInd messages (OCP Protocol v4.1.1 Extension)
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


	private static final Logger LOGGER = LoggerFactory.getLogger(HelloMessageFactory.class);

	@Override
    public HelloMessage deserialize(List<Object> rawMessage) {
        HelloMessageBuilder builder = new HelloMessageBuilder();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("HelloMessageFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().contains("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.debug("HelloMessageFactory - getMsgType = " + builder.getMsgType());
                    }
                    //msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object t_tok = itr.next();
                        int uid_tok = Integer.parseInt(((XmlCharacters)t_tok).data().toString());
                        builder.setXid((long)uid_tok);
                        LOGGER.debug("HelloMessageFactory - setXid " + builder.getXid());
                    }
                    //version
                    else if (((XmlElementStart)tok).name().equals("version")){
                        String rel_ver = (((XmlCharacters)itr.next()).data()).toString();
                        builder.setVersion(rel_ver);
                        LOGGER.debug("HelloMessageFactory - setVersion " + builder.getVersion());
                    }
                    //versionId
                    else if (((XmlElementStart)tok).name().equals("vendorId")){
                        String rel_verId = (((XmlCharacters)itr.next()).data()).toString();
                        builder.setVendorId(rel_verId);
                        LOGGER.debug("HelloMessageFactory - setVendorId " + builder.getVendorId());
                    }
                    //serialNumber
                    else if (((XmlElementStart)tok).name().equals("serialNumber")){
                        String rel_serN = (((XmlCharacters)itr.next()).data()).toString();
                        builder.setSerialNumber(rel_serN);
                        LOGGER.debug("HelloMessageFactory - setSerialNumber " + builder.getSerialNumber());
                    }
                } 
            }
            catch( Exception t ) {
                 LOGGER.error("Error " + tok + " " + t.toString());
            }
        }

        LOGGER.debug("HelloMessageFactory - Builder: " + builder.build());
        return builder.build();
    }
}
