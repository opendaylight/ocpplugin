/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChangeInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChangeIndBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateVal;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.utils.MessageHelper;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates StateChangeInd message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/* limitation: objId:1, state(type, value):1 */

/*
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>IND</msgType>
        <msgUID>34570</msgUID>
    </header>
    <body>
        <stateChangeInd>
            <obj objID="exampleObj:0">
                <state type="AST">UNLOCKED</state>
            </obj>
        </stateChangeInd>
    </body>
</msg>
*/

public class StateChangeFactory implements OCPDeserializer<StateChangeInd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateChangeFactory.class);

    @Override
    public StateChangeInd deserialize(List<Object> rawMessage) {
        StateChangeIndBuilder builder = new StateChangeIndBuilder();
        Iterator itr = rawMessage.iterator();
 
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("StateChangeFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                    //msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        String type = MessageHelper.getMsgType(itr);
                        builder.setMsgType(OcpMsgType.valueOf(type));
                    }
                    //msgUID
                    if (((XmlElementStart)tok).name().equals("msgUID")){
                        String uidStr = MessageHelper.getMsgUID(itr);
                        int uid = Integer.parseInt(uidStr);
                        builder.setXid((long)uid);
                    }
                    //obj
                    if (((XmlElementStart)tok).name().equals("obj")) {

                        //set Obj ID
                        builder.setObjId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));

                        Object objtok = itr.next();
                        while(!(objtok instanceof XmlElementStart)){
                            objtok = itr.next();
                        }
                        
                        while(objtok instanceof XmlElementStart) {
                            if(((XmlElementStart)objtok).name().equals("state")) {
                                //set state Name                       
                                String tmp = ((XmlElementStart)objtok).attributes().get(0).value();                                
                                builder.setStateType(StateType.valueOf(tmp));
                                LOGGER.debug("StateChangeFactory - builder getStateType = " + builder.getStateType());
                                
                            	//set state Value
                                String bufStr = MessageHelper.getMsgUID(itr);
                                builder.setStateValue(StateVal.valueOf(bufStr));
                                LOGGER.debug("StateChangeFactory - builder getStateValue = " + builder.getStateValue());
                                
                                //skip remain of XmlCharacters
                                objtok = itr.next();
                                while((objtok instanceof XmlCharacters)){
                                    objtok = itr.next(); 
                                }
                                LOGGER.info("StateChangeFactory - found next: {}", objtok);
                            }

                            if (objtok instanceof XmlElementEnd) {
                                LOGGER.debug("StateChangeFactory - found XmlElementEnd: {}", objtok);
                                if(((XmlElementEnd)objtok).name().equals("obj")) {
                                    LOGGER.debug("StateChangeFactory - objtok = " + objtok);
                                }
                            }
                            else{
                                while(!(objtok instanceof XmlElementStart)){
                                    objtok = itr.next();
                                }
                            }
                        }
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        LOGGER.debug("StateChangeFactory - Builder: " + builder.build());
        return builder.build();
    }
}
