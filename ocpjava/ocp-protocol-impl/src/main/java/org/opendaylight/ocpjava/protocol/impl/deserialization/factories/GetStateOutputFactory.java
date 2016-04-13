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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateVal;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetStateRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getstateoutput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getstateoutput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getstateoutput.obj.State;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getstateoutput.obj.StateBuilder;
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
 * Translates GetParamOutput messages (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */


/*
<!-- Example: Object State Reporting Response -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>34561</msgUID>
    </header>
    <body>
        <getStateResp>
            <result>SUCCESS</result>
            <obj objID="exampleObj:0">
                <state type="FST">OPERATIONAL</state>
                <state type="AST">UNLOCKED</state>
            </obj>
        </getStateResp>
    </body>
</msg>

*/

public class GetStateOutputFactory implements OCPDeserializer<GetStateOutput> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetStateOutputFactory.class);
    @Override
    public GetStateOutput deserialize(List<Object> rawMessage) {
        GetStateOutputBuilder builder = new GetStateOutputBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        StateBuilder statebuilder = new StateBuilder();

        List<State> llist_state = new ArrayList();
        List<Obj> llist_obj = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("GetStateOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.trace("GetStateOutputFactory - getMsgType = " + builder.getMsgType());
                    }  
                	//msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object t_tok = itr.next();
                        int uid_tok = Integer.parseInt(((XmlCharacters)t_tok).data().toString());
                        builder.setXid((long)uid_tok);
                        LOGGER.trace("GetStateOutputFactory - getXid = " + builder.getXid());
                    }
                    //result
                    else if (((XmlElementStart)tok).name().equals("result")){
                        String rel = ((XmlCharacters)itr.next()).data().replace("_", "").toString();
                        builder.setResult(GetStateRes.valueOf(rel));
                        LOGGER.trace("GetStateOutputFactory - getResult = " + builder.getResult());
                    }
                    //obj
                    else if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                        objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        LOGGER.trace("GetStateOutputFactory - getId = " + objbuilder.getId());

                        Object o_tok = itr.next();
                        while(!(o_tok instanceof XmlElementStart))
                        	o_tok = itr.next();

                        while( !(((XmlElementStart)o_tok).name().equals("obj")) ) {
                            if(((XmlElementStart)o_tok).name().equals("state")) {
                                //set state Name                       
                                String tmp = ((XmlElementStart)o_tok).attributes().get(0).value();                                
                                statebuilder.setName(StateType.valueOf(tmp));
                                LOGGER.trace("GetStateOutputFactory - getName = " + statebuilder.getName());

                            	//set state Value
                                Object c_tok = itr.next();   
                            	StringBuffer buf = new StringBuffer();
                            	while(c_tok instanceof XmlCharacters) {
                            	    buf.append(((XmlCharacters)c_tok).data().toString());
                            	    c_tok = itr.next();
                            	}
                            	statebuilder.setValue(StateVal.valueOf(buf.toString().replace("_", "")));
                                
                                LOGGER.trace("GetStateOutputFactory - getValue = " + statebuilder.getValue());
                                llist_state.add(statebuilder.build());
                                statebuilder = new StateBuilder();
                            }
                            itr.next();
                            o_tok = itr.next();
                            if (o_tok instanceof XmlElementEnd) {
                                LOGGER.trace("GetStateOutputFactory - found XmlElementEnd: {}", o_tok);
                            	if(((XmlElementEnd)o_tok).name().equals("obj")) {
                            	    o_tok = itr.next();
                            	    o_tok = itr.next();
                            	    break;
                            	}
                            }
                            while(!(o_tok instanceof XmlElementStart))
                                o_tok = itr.next();
                        }
                        objbuilder.setState(llist_state);                    
                        LOGGER.trace("GetStateOutputFactory - objbuilder.build() = " + objbuilder.build());
                        llist_obj.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(llist_obj);
        LOGGER.debug("GetStateOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
