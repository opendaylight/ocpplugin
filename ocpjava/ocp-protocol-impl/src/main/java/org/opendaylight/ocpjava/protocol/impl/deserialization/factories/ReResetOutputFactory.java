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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates ReReset messages (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: ReReset Response -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>2345</msgUID>
    </header>
    <body>
        <resetResp>
            <result>SUCCESS</result>
        </resetResp>
    </body>
</msg>
*/

public class ReResetOutputFactory implements OCPDeserializer<ReResetOutput> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReResetOutputFactory.class);
    @Override
    public ReResetOutput deserialize(List<Object> rawMessage) {
        ReResetOutputBuilder builder = new ReResetOutputBuilder();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("ReResetOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        //XmlCharacters of body
                        itr.next(); 
                        //XmlElementStart of msgType
                    	Object type = itr.next(); 
                        if (type instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)type).name().toUpperCase()));
                        LOGGER.debug("ReResetOutputFactory - getMsgType = " + builder.getMsgType());
                    }
                	//msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object uidtok = itr.next();
                        int uid = Integer.parseInt(((XmlCharacters)uidtok).data().toString());
                        builder.setXid((long)uid);
                        LOGGER.debug("ReResetOutputFactory - getXid = " + builder.getXid());
                    }
                    //result
                    else if (((XmlElementStart)tok).name().equals("result")){
                        String rel = (((XmlCharacters)itr.next()).data()).replace("_", "").toString();
                        builder.setResult(OriRes.valueOf(rel));
                        LOGGER.debug("ReResetOutputFactory - getResult = " + builder.getResult());
                    }
                } 
            }
            catch( Exception t ) {
                 LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        LOGGER.debug("ReResetOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
