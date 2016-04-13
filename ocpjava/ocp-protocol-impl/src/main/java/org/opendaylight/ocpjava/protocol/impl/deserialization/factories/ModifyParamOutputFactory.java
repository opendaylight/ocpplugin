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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamGlobRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifyparamoutput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifyparamoutput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifyparamoutput.obj.Param;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifyparamoutput.obj.ParamBuilder;
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
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Modify Parameter Response (multiple parameters, failure) -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>7894</msgUID>
    </header>
    <body>
        <modifyParamResp>
            <globResult>FAIL_PARAMETER_FAIL</globResult>
            <obj objID="exampleObj:1">
                <param name="parameter 1"/>
                <result>SUCCESS</result>
                <param name="parameter 2"/>
                <result>SUCCESS</result>
                <param name="parameter 3"/>
                <result>FAIL_VALUE_TYPE_ERROR</result>
            </obj>
        </modifyParamResp>
    </body>
</msg>

*/

public class ModifyParamOutputFactory implements OCPDeserializer<ModifyParamOutput> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyParamOutputFactory.class);
    @Override
    public ModifyParamOutput deserialize(List<Object> rawMessage) {
        ModifyParamOutputBuilder builder = new ModifyParamOutputBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        ParamBuilder parambuilder = new ParamBuilder();

        List<Param> llist_param = new ArrayList();
        List<Obj> llist_obj = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("ModifyParamOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                    //msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.debug("ModifyParamOutputFactory - getMsgType = " + builder.getMsgType());
                    }
                    //msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                    	Object t_tok = itr.next();   
                    	StringBuffer buf = new StringBuffer();
                    	while(t_tok instanceof XmlCharacters) {
                    	    buf.append(((XmlCharacters)t_tok).data().toString());
                    	    t_tok = itr.next();
                    	}
                        int uid_tok = Integer.parseInt(buf.toString());
                        builder.setXid((long)uid_tok);
                        LOGGER.debug("ModifyParamOutputFactory - getXid = " + builder.getXid());
                    }
                    //globResult
                    else if (((XmlElementStart)tok).name().equals("globResult")){
                    	Object t_tok = itr.next();   
                    	StringBuffer buf = new StringBuffer();
                    	while(t_tok instanceof XmlCharacters) {
                    	    buf.append(((XmlCharacters)t_tok).data().toString());
                    	    t_tok = itr.next();
                    	}
                        builder.setGlobResult(ModifyParamGlobRes.valueOf(buf.toString().replace("_", "")));
                        LOGGER.debug("ModifyParamOutputFactory - getGlobResult = " + builder.getGlobResult());
                    }
                    //obj
                    else if (((XmlElementStart)tok).name().equals("obj")) {
                        if(((XmlElementStart)tok).attributes().size() >= 1)
                    	    objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        LOGGER.debug("ModifyParamOutputFactory - getId = " + objbuilder.getId());

                        tok = itr.next();                                                	
                        while(!(tok instanceof XmlElementStart)) {
                    	    tok = itr.next();
                            //LOGGER.trace("ModifyParamOutputFactory - test (tok instanceof XmlElementStart) = " + (tok instanceof XmlElementStart));
                        }                        
                        while(!(((XmlElementStart)tok).name().equals("obj"))){
                        	if(((XmlElementStart)tok).name().equals("param")) {
                                //param name
                                parambuilder.setName(((XmlElementStart)tok).attributes().get(0).value());
                                LOGGER.debug("ModifyParamOutputFactory - getName = " + parambuilder.getName());

                                Object r_tok = itr.next();
                                while(!(r_tok instanceof XmlElementStart))
                                    r_tok = itr.next();
                                
                                 //param result
                                if(((XmlElementStart)r_tok).name().equals("result")) {

                                    Object rr_tok = itr.next();   
                                    StringBuffer buf = new StringBuffer();
                                    while(rr_tok instanceof XmlCharacters) {
                                        buf.append(((XmlCharacters)rr_tok).data().toString());
                                        rr_tok = itr.next();
                                    }
                                    parambuilder.setResult(ModifyParamRes.valueOf(buf.toString().replace("_", "")));
                                    LOGGER.debug("ModifyParamOutputFactory - getResult = " + parambuilder.getResult());
                            	}                       	
                                llist_param.add(parambuilder.build());
                                parambuilder = new ParamBuilder();

                                //jump to the next token until the token is param XmlElementStart or obj XmlElementEnd
                                tok = itr.next();
                                while((tok instanceof XmlElementStart)||(tok instanceof XmlElementEnd)||(tok instanceof XmlCharacters)) {
                                    if (tok instanceof XmlElementStart) {
                                        if(((XmlElementStart)tok).name().equals("param"))
                                            break;
                                    }
                                    else if (tok instanceof XmlElementEnd) {
                                        if(((XmlElementEnd)tok).name().equals("obj"))
                                            break;
                                    }
                                    tok = itr.next();
                                }

                                if (tok instanceof XmlElementEnd) {
                                    if(((XmlElementEnd)tok).name().equals("obj"))
                                        break;
                                }
                            }
                        }
                        objbuilder.setParam(llist_param);                    
                        llist_param = new ArrayList();
                        LOGGER.trace("ModifyParamOutputFactory - objbuilder.build() = " + objbuilder.build());                        
                        llist_obj.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(llist_obj);
        LOGGER.debug("ModifyParamOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
