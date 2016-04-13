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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjGlobRes;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.createobjoutput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.createobjoutput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.createobjoutput.obj.Param;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.createobjoutput.obj.ParamBuilder;
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
 * Translates CreateObjOutput messages (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Object Creation Response (multiple parameters, parameter failure) -->
<?xml version="1.0" encoding="utf-8"?>
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>8937</msgUID>
    </header>
    <body>
        <createObjResp>
            <globResult>FAIL_PARAMETER_FAIL</globResult>
            <obj objID="exampleObj:0">
                <param name="parameter 1"/>
                <result>SUCCESS</result>
                <param name="parameter 2"/>
                <result>SUCCESS</result>
                <param name="parameter 3"/>
                <result>FAIL_VALUE_TYPE_ERROR</result>
            </obj>
        </createObjResp>
    </body>
</msg>

*/

public class CreateObjOutputFactory implements OCPDeserializer<CreateObjOutput> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateObjOutputFactory.class);
    @Override
    public CreateObjOutput deserialize(List<Object> rawMessage) {
        CreateObjOutputBuilder builder = new CreateObjOutputBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        ParamBuilder parambuilder = new ParamBuilder();

        List<Param> llist_param = new ArrayList();
        List<Obj> llist_obj = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("CreateObjOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.debug("CreateObjOutputFactory - getMsgType = " + builder.getMsgType());
                    }
                	//msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object t_tok = itr.next();
                        int uid_tok = Integer.parseInt(((XmlCharacters)t_tok).data().toString());
                        builder.setXid((long)uid_tok);
                        LOGGER.debug("CreateObjOutputFactory - getXid = " + builder.getXid());
                    }
                    //globResult
                    else if (((XmlElementStart)tok).name().equals("globResult")){
                        String rel = (((XmlCharacters)itr.next()).data()).toString().replace("_", "");
                        LOGGER.debug("CreateObjOutputFactory - globResult = " + rel);
                        builder.setGlobResult(CreateObjGlobRes.valueOf(rel));
                    }
                    //obj
                    else if (((XmlElementStart)tok).name().equals("obj")) {
                    	//set Obj ID
                    	if(((XmlElementStart)tok).attributes().size() >= 1)
                    	    objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));

                    	//find the next param of XmlElementStart 
                        Object p_tok = itr.next();
                        while(!(p_tok instanceof XmlElementStart)) {
                            if(p_tok instanceof XmlElementEnd){
                                //if obj of XmlElementEnd found, it means there is no param in this object
                                if(((XmlElementEnd)p_tok).name().equals("obj"))
                                    break;
                            }
                            p_tok = itr.next();
                        }
                    	
                    	if (p_tok instanceof XmlElementStart) {
	                    	while(!(((XmlElementStart)p_tok).name().equals("obj"))){
	                    		if(((XmlElementStart)p_tok).name().equals("param")) {
	                                parambuilder.setName(((XmlElementStart)p_tok).attributes().get(0).value());
	                               
	                                Object r_tok = itr.next();
	                                while(!(r_tok instanceof XmlElementStart))
	                                    r_tok = itr.next();

	                                if(((XmlElementStart)r_tok).name().equals("result")) {
	                                	Object rr_tok = itr.next();
	                                	parambuilder.setResult(CreateObjRes.valueOf(((XmlCharacters)rr_tok).data().toString().replace("_", "")));
	                                }
	                                LOGGER.trace("CreateObjOutputFactory - parambuilder.build() " + parambuilder.build());
	                                llist_param.add(parambuilder.build());
	                                parambuilder = new ParamBuilder();
	                                
	                                //jump to the next token until the token is param XmlElementStart or obj XmlElementEnd
	                                p_tok = itr.next();
	                                while((p_tok instanceof XmlElementStart)||(p_tok instanceof XmlElementEnd)||(p_tok instanceof XmlCharacters)) {
	                                    if (p_tok instanceof XmlElementStart) {
	                                            if(((XmlElementStart)p_tok).name().equals("param"))
	                                                    break;
	                                    }
	                                    else if (p_tok instanceof XmlElementEnd) {
	                                            if(((XmlElementEnd)p_tok).name().equals("obj"))
	                                                    break;
	                                    }
	                                    p_tok = itr.next();
	                                }
	                                
	                                if (p_tok instanceof XmlElementEnd) {
	                                	if(((XmlElementEnd)p_tok).name().equals("obj"))
	                                		break;
	                                }
	                    	    }
	                    	}
	                        objbuilder.setParam(llist_param);
                    	}                  
                        LOGGER.trace("CreateObjOutputFactory - objbuilder.build() " + objbuilder.build());
                        llist_obj.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(llist_obj);
        LOGGER.debug("CreateObjOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}