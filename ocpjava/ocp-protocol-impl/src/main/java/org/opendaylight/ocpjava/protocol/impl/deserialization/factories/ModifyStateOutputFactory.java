/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateVal;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyStateRes;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateoutput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateoutput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateoutput.obj.State;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateoutput.obj.StateBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates ModifyStateOutput messages (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Object State Modification Response -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>34570</msgUID>
    </header>
    <body>
        <modifyStateResp>
            <result>SUCCESS</result>
            <obj objID="exampleObj:0">
                <state type="AST">UNLOCKED</state>
            </obj>
            <obj objID="exampleObj:1">
                <state type="AST">UNLOCKED</state>
            </obj>
        </modifyStateResp>
    </body>
</msg>
*/

public class ModifyStateOutputFactory implements OCPDeserializer<ModifyStateOutput> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModifyStateOutputFactory.class);
    @Override
    public ModifyStateOutput deserialize(List<Object> rawMessage) {
        ModifyStateOutputBuilder builder = new ModifyStateOutputBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        StateBuilder statebuilder = new StateBuilder();

        List<State> statelist = new ArrayList();
        List<Obj> objlist = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("ModifyStateOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                    //msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        Object type = itr.next();
                        while(!(type instanceof XmlElementStart)){
                            type = itr.next();
                        }
                        builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)type).name().toUpperCase()));
                        LOGGER.debug("ModifyStateOutputFactory - getMsgType = " + builder.getMsgType());
                    }
                    //msgUID
                    if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object uidtok = itr.next();   
                        StringBuilder buf = new StringBuilder();
                    	while(uidtok instanceof XmlCharacters) {
                    		buf.append(((XmlCharacters)uidtok).data().toString());
                    		uidtok = itr.next();
                    	}
                        int uid = Integer.parseInt(buf.toString());
                        builder.setXid((long)uid);
                        LOGGER.debug("ModifyStateOutputFactory - getXid = " + builder.getXid());
                    }
                    //result
                    if (((XmlElementStart)tok).name().equals("result")){
                     
                    	Object reltok = itr.next();   
                    	StringBuilder buf = new StringBuilder();
                    	while(reltok instanceof XmlCharacters) {
                    		buf.append(((XmlCharacters)reltok).data().toString());
                    		reltok = itr.next();
                    	}
                        builder.setResult(ModifyStateRes.valueOf(buf.toString().replace("_", "")));
                        LOGGER.debug("ModifyStateOutputFactory - getResult = " + builder.getResult());   
                 
                    }
                    //obj
                    if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                        objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        LOGGER.debug("ModifyStateOutputFactory - objbuilder getId = " + objbuilder.getId());

                        Object objtok = itr.next();
                        while(!(objtok instanceof XmlElementStart)){
                            objtok = itr.next();
                        }

                        while( !(((XmlElementStart)objtok).name().equals("obj")) ) {
                            if(((XmlElementStart)objtok).name().equals("state")) {
                                //set state Name                       
                                String tmp = ((XmlElementStart)objtok).attributes().get(0).value();                                
                                statebuilder.setName(StateType.valueOf(tmp));
                                LOGGER.debug("ModifyStateOutputFactory - statebuilder getName = " + statebuilder.getName());

                                //set state Value
                            	Object statetok = itr.next();   
                            	StringBuilder buf = new StringBuilder();
                            	while(statetok instanceof XmlCharacters) {
                            		buf.append(((XmlCharacters)statetok).data().toString());
                            		statetok = itr.next();
                            	}
                            	statebuilder.setValue(StateVal.valueOf(buf.toString().replace("_", "")));
                                LOGGER.debug("ModifyStateOutputFactory - statebuilder getValue = " + statebuilder.getValue());

                                statelist.add(statebuilder.build());
                                
                                tok = itr.next();
                                tok = itr.next();
                                if ((tok instanceof XmlElementEnd) && ((XmlElementEnd)tok).name().equals("obj")) {
                               	    break;
                                }
                            }
                        }
                        objbuilder.setState(statelist);                    
                        statelist = new ArrayList();
                        LOGGER.trace("ModifyStateOutputFactory - objbuilder.build(): " + objbuilder.build());
                        objlist.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(objlist);
        LOGGER.debug("ModifyStateOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
