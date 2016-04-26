/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import java.util.List;
import java.util.Iterator;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates Hello messages (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Health Check Response -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>0</msgUID>
    </header>
    <body>
        <healthCheckResp>
            <result>SUCCESS</result>
        </healthCheckResp>
    </body>
</msg>
*/

public class HealthCheckOutputFactory implements OCPDeserializer<HealthCheckOutput> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckOutputFactory.class);
    @Override
    public HealthCheckOutput deserialize(List<Object> rawMessage) {
        HealthCheckOutputBuilder builder = new HealthCheckOutputBuilder();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("HealthCheckOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        //XmlCharacters of body
                        itr.next();
                        //XmlElementStart of msgType
                    	Object type = itr.next(); 
                        if (type instanceof XmlElementStart){
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)type).name().toUpperCase()));
                    	}
                        LOGGER.debug("HealthCheckOutputFactory - getMsgType = " + builder.getMsgType());
                    }
                	//msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        //XmlCharacters of msgUID
                        Object uidtok = itr.next();
                        if (uidtok instanceof XmlCharacters) {
                        int uid = Integer.parseInt(((XmlCharacters)uidtok).data().toString());
                        builder.setXid((long)uid);
                    }
                        LOGGER.debug("HealthCheckOutputFactory - getXid = " + builder.getXid());
                    }
                    //result
                    else if (((XmlElementStart)tok).name().equals("result")){
                        //XmlCharacters of result
                        String rel = (((XmlCharacters)itr.next()).data()).replace("_", "").toString(); 
                        builder.setResult(OriRes.valueOf(rel));
                        LOGGER.debug("HealthCheckOutputFactory - getResult = " + builder.getResult());
                    }
                } 
            }
            catch( Exception t ) {
                 LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        LOGGER.debug("HealthCheckOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
