/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import io.netty.buffer.ByteBuf;
//import io.netty.handler.codec.xml.*;

import java.util.Iterator;
import java.util.List;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates SetTime messages (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Set Time Response -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>1235</msgUID>
    </header>
    <body>
        <setTimeResp>
            <result>SUCCESS</result>
        </setTimeResp>
    </body>
</msg>

*/


public class SetTimeOutputFactory implements OCPDeserializer<SetTimeOutput> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetTimeOutputFactory.class);

    @Override
    public SetTimeOutput deserialize(List<Object> rawMessage) {
        SetTimeOutputBuilder builder = new SetTimeOutputBuilder();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("SetTimeOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.debug("SetTimeOutputFactory - getMsgType = " + builder.getMsgType());
                    }
                	//msgUID
                    if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object t_tok = itr.next();
                        int uid_tok = Integer.parseInt(((XmlCharacters)t_tok).data().toString());
                        builder.setXid((long)uid_tok);
                        LOGGER.debug("SetTimeOutputFactory - getXid = " + builder.getXid());
                    }
                    //result
                    if (((XmlElementStart)tok).name().equals("result")){
                        String rel = (((XmlCharacters)itr.next()).data()).replace("_", "").toString();
                        builder.setResult(OriRes.valueOf(rel));
                        LOGGER.debug("SetTimeOutputFactory - getResult = " + builder.getResult());
                    }
                } 
            }
            catch( Exception t ) {
                 LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        LOGGER.debug("SetTimeOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
