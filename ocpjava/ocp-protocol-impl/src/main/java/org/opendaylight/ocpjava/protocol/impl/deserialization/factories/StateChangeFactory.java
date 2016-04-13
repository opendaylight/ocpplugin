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

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutputBuilder;
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

        List<State> llist_state = new ArrayList();
        List<Obj> llist_obj = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("StateChangeFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.debug("StateChangeFactory - getMsgType = " + builder.getMsgType());
                    }                	
                	//msgUID
                    if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object t_tok = itr.next();
                        int uid_tok = Integer.parseInt(((XmlCharacters)t_tok).data().toString());
                        builder.setXid((long)uid_tok);
                        LOGGER.debug("StateChangeFactory - getXid = " + builder.getXid());
                    }
                    //obj
                    if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                        objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        itr.next();
                        Object o_tok = itr.next();
                        while( !(((XmlElementStart)o_tok).name().equals("obj")) ) {
                            if(((XmlElementStart)o_tok).name().equals("state")) {
                                //set state Name                       
                                String tmp = ((XmlElementStart)o_tok).attributes().get(0).value();                                
                                statebuilder.setName(StateType.valueOf(tmp));
                                LOGGER.debug("StateChangeFactory - statebuilder getName = " + statebuilder.getName());
                                
                            	//set state Value
                                Object c_tok = itr.next();   
                            	StringBuffer buf = new StringBuffer();
                            	while(c_tok instanceof XmlCharacters) {
                            		buf.append(((XmlCharacters)c_tok).data().toString());
                            		c_tok = itr.next();
                            	}
                            	statebuilder.setValue(StateVal.valueOf(buf.toString().replace("_", "")));
                                LOGGER.debug("StateChangeFactory - statebuilder getValue = " + statebuilder.getValue());
                                
                                llist_state.add(statebuilder.build());

                                o_tok = itr.next();
                                o_tok = itr.next();
                            	LOGGER.trace("CreateObjOutputFactory - o_tok 3 " + o_tok);
                                if (o_tok instanceof XmlElementEnd) {
                                	if(((XmlElementEnd)o_tok).name().equals("obj"))
                                		break;
                                }
                            }
                        }
                        objbuilder.setState(llist_state);                    
                        llist_obj.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(llist_obj);
        LOGGER.debug("StateChangeFactory - Builder: " + builder.build());
        return builder.build();
    }
}
