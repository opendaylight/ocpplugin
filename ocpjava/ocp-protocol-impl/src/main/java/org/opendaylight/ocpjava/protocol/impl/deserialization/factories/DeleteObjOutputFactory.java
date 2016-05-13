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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.DeleteObjRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.utils.MessageHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates DeleteObj messages
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */


/*
<!-- Example: Object Deletion Response -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>1234</msgUID>
    </header>
    <body>
        <deleteObjResp>
            <result>SUCCESS</result>
        </deleteObjResp>
    </body>
</msg>

*/


public class DeleteObjOutputFactory implements OCPDeserializer<DeleteObjOutput> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteObjOutputFactory.class);

    @Override
    public DeleteObjOutput deserialize(List<Object> rawMessage) {
    	DeleteObjOutputBuilder builder = new DeleteObjOutputBuilder();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("DeleteObjOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                    //msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        String typeStr = MessageHelper.getMsgType(itr);
                        builder.setMsgType(OcpMsgType.valueOf(typeStr));
                    }
                    //msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        String uidStr = MessageHelper.getMsgUID(itr);
                        int uid = Integer.parseInt(uidStr);
                        builder.setXid((long)uid);
                    }
                    //result
                    else if (((XmlElementStart)tok).name().equals("result")){
                        String rel = MessageHelper.getResult(itr);
                        builder.setResult(DeleteObjRes.valueOf(rel));
                    }
                } 
            }
            catch( Exception t ) {
                 LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        LOGGER.debug("DeleteObjOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
