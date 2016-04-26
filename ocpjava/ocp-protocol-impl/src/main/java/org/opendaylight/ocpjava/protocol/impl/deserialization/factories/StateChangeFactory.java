/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChange;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChangeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateVal;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.statechangesource.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.statechangesource.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.statechangesource.obj.State;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.statechangesource.obj.StateBuilder;
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
 * Translates StateChange (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

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

public class StateChangeFactory implements OCPDeserializer<StateChange> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StateChangeFactory.class);
    @Override
    public StateChange deserialize(List<Object> rawMessage) {
        StateChangeBuilder builder = new StateChangeBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        StateBuilder statebuilder = new StateBuilder();

        List<State> statelist = new ArrayList();
        List<Obj> objlist = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("StateChangeFactory - itr = " + tok);
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
                        LOGGER.debug("StateChangeFactory - getMsgType = " + builder.getMsgType());
                    }                	
                	//msgUID
                    if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object uidtok = itr.next();
                        int uid = Integer.parseInt(((XmlCharacters)uidtok).data().toString());
                        builder.setXid((long)uid);
                        LOGGER.debug("StateChangeFactory - getXid = " + builder.getXid());
                    }
                    //obj
                    if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                        objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        itr.next();
                        Object objtok = itr.next();
                        while( !(((XmlElementStart)objtok).name().equals("obj")) ) {
                            if(((XmlElementStart)objtok).name().equals("state")) {
                                //set state Name                       
                                String tmp = ((XmlElementStart)objtok).attributes().get(0).value();                                
                                statebuilder.setName(StateType.valueOf(tmp));
                                LOGGER.debug("StateChangeFactory - statebuilder getName = " + statebuilder.getName());
                                
                            	//set state Value
                                Object statetok = itr.next();   
                                StringBuilder buf = new StringBuilder();
                            	while(statetok instanceof XmlCharacters) {
                            		buf.append(((XmlCharacters)statetok).data().toString());
                            		statetok = itr.next();
                            	}
                            	statebuilder.setValue(StateVal.valueOf(buf.toString().replace("_", "")));
                                LOGGER.debug("StateChangeFactory - statebuilder getValue = " + statebuilder.getValue());
                                
                                statelist.add(statebuilder.build());

                                objtok = itr.next();
                                objtok = itr.next();
                            	LOGGER.trace("CreateObjOutputFactory - objtok 3 " + objtok);
                                if (objtok instanceof XmlElementEnd) {
                                	if(((XmlElementEnd)objtok).name().equals("obj"))
                                		break;
                                }
                            }
                        }
                        objbuilder.setState(statelist);                    
                        objlist.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.info("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(objlist);
        LOGGER.debug("StateChangeFactory - Builder: " + builder.build());
        return builder.build();
    }
}
