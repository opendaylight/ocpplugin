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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder;
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
<!-- Example: Get Parameter Response (single object, all parameters -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>7891</msgUID>
    </header>
    <body>
        <getParamResp>
            <result>SUCCESS</result>
            <obj objID="RE:0">
                <param name="vendorID">xyz</param>
                <param name="productID">acme0815</param>
                <param name="productRev">A.01</param>
            </obj>
        </getParamResp>
    </body>
</msg>

*/


public class GetParamOutputFactory implements OCPDeserializer<GetParamOutput> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetParamOutputFactory.class);
    @Override
    public GetParamOutput deserialize(List<Object> rawMessage) {
        GetParamOutputBuilder builder = new GetParamOutputBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        ParamBuilder parambuilder = new ParamBuilder();

        List<Param> llist_param = new ArrayList();
        List<Obj> llist_obj = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("GetParamOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.debug("GetParamOutputFactory - getMsgType = " + builder.getMsgType());
                    }  
                    //msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object t_tok = itr.next();
                        int uid_tok = Integer.parseInt(((XmlCharacters)t_tok).data().toString());
                        builder.setXid((long)uid_tok);
                        LOGGER.debug("GetParamOutputFactory - getXid = " + builder.getXid());
                    }
                    //result
                    else if (((XmlElementStart)tok).name().equals("result")){
                        String rel = ((XmlCharacters)itr.next()).data().replace("_", "").toString();
                        builder.setResult(GetParamRes.valueOf(rel));
                        LOGGER.debug("GetParamOutputFactory - getResult = " + builder.getResult());
                    }
                    //obj
                    else if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                    	if(((XmlElementStart)tok).attributes().size() >= 1)
                            objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        LOGGER.debug("GetParamOutputFactory - objbuilder getId = " + objbuilder.getId());

                        Object o_tok = itr.next();
                        while(!(o_tok instanceof XmlElementStart))
                        	o_tok = itr.next();

                        while( !(((XmlElementStart)o_tok).name().equals("obj")) ) {
                            if(((XmlElementStart)o_tok).name().equals("param")) {
                                //set param Name 
                            	if (((XmlElementStart)o_tok).attributes().size() >= 1)
                                    parambuilder.setName(((XmlElementStart)o_tok).attributes().get(0).value());
                                LOGGER.debug("GetParamOutputFactory - parambuilder getName = " + parambuilder.getName());
                                
                                //set param Value
                            	Object c_tok = itr.next(); //get param character(content)      
                            	StringBuffer buf = new StringBuffer();
                            	while(c_tok instanceof XmlCharacters) {
                            		buf.append(((XmlCharacters)c_tok).data().toString());
                            		c_tok = itr.next();
                            	}
                            	parambuilder.setValue(buf.toString());
                                
                                LOGGER.debug("GetParamOutputFactory - parambuilder getValue = " + parambuilder.getValue());
                                llist_param.add(parambuilder.build());
                                parambuilder = new ParamBuilder();
                            }

                            //jump to the next token until the token is param XmlElementStart or obj XmlElementEnd
                            o_tok = itr.next();
                            while((o_tok instanceof XmlElementStart)||(o_tok instanceof XmlElementEnd)||(o_tok instanceof XmlCharacters)) {
                                if (o_tok instanceof XmlElementStart) {
                                        if(((XmlElementStart)o_tok).name().equals("param"))
                                                break;
                                }
                                else if (o_tok instanceof XmlElementEnd) {
                                        if(((XmlElementEnd)o_tok).name().equals("obj"))
                                                break;
                                }
                                o_tok = itr.next();
                            }

                            if (o_tok instanceof XmlElementEnd) {
                            	if(((XmlElementEnd)o_tok).name().equals("obj"))
                            		break;
                            }
                        }
                        objbuilder.setParam(llist_param);                    
                        llist_param = new ArrayList();
                        LOGGER.trace("GetParamOutputFactory - objbuilder.build(): " + objbuilder.build());
                        llist_obj.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(llist_obj);
        LOGGER.debug("GetParamOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
