/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

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

        List<Param> paramlist = new ArrayList();
        List<Obj> objlist = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("ModifyParamOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                    //msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        //XmlCharacters of body
                        itr.next();
                        //XmlElementStart of msgType
                    	Object type = itr.next();
                        if (type instanceof XmlElementStart){
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)type).name().toUpperCase()));
                    	}
                        LOGGER.debug("ModifyParamOutputFactory - getMsgType = " + builder.getMsgType());
                    }
                    //msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                    	Object uidtok = itr.next();   
                    	StringBuilder buf = new StringBuilder();
                    	while(uidtok instanceof XmlCharacters) {
                    	    buf.append(((XmlCharacters)uidtok).data().toString());
                    	    uidtok = itr.next();
                    	}
                        int uid = Integer.parseInt(buf.toString());
                        builder.setXid((long)uid);
                        LOGGER.debug("ModifyParamOutputFactory - getXid = " + builder.getXid());
                    }
                    //globResult
                    else if (((XmlElementStart)tok).name().equals("globResult")){
                    	Object reltok = itr.next();   
                    	StringBuilder buf = new StringBuilder();
                    	while(reltok instanceof XmlCharacters) {
                    	    buf.append(((XmlCharacters)reltok).data().toString());
                    	    reltok = itr.next();
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
                        }                        
                        while(!(((XmlElementStart)tok).name().equals("obj"))){
                        	if(((XmlElementStart)tok).name().equals("param")) {
                                //param name
                                parambuilder.setName(((XmlElementStart)tok).attributes().get(0).value());
                                LOGGER.debug("ModifyParamOutputFactory - getName = " + parambuilder.getName());

                                Object nexttok = itr.next();
                                while(!(nexttok instanceof XmlElementStart)){
                                    nexttok = itr.next();
                                }
                                
                                //param result
                                if(((XmlElementStart)nexttok).name().equals("result")) {
                                    Object ptok = itr.next();   
                                    StringBuilder buf = new StringBuilder();
                                    while(ptok instanceof XmlCharacters) {
                                        buf.append(((XmlCharacters)ptok).data().toString());
                                        ptok = itr.next();
                                    }
                                    parambuilder.setResult(ModifyParamRes.valueOf(buf.toString().replace("_", "")));
                                    LOGGER.debug("ModifyParamOutputFactory - getResult = " + parambuilder.getResult());
                            	}                       	
                                paramlist.add(parambuilder.build());
                                parambuilder = new ParamBuilder();

                                //jump to the next token until the token is param XmlElementStart or obj XmlElementEnd
                                tok = itr.next();
                                while((tok instanceof XmlElementStart)||(tok instanceof XmlElementEnd)||(tok instanceof XmlCharacters)) {
                                    if ((tok instanceof XmlElementStart) &&((XmlElementStart)tok).name().equals("param")) {
                                        break;
                                    }
                                    else if ((tok instanceof XmlElementEnd) &&((XmlElementEnd)tok).name().equals("obj")){
                                        break;
                                    }
                                    tok = itr.next();
                                }

                                if ((tok instanceof XmlElementEnd) && ((XmlElementEnd)tok).name().equals("obj")) {
                                    break;
                                }
                            }
                        }
                        objbuilder.setParam(paramlist);                    
                        paramlist = new ArrayList();
                        LOGGER.trace("ModifyParamOutputFactory - objbuilder.build() = " + objbuilder.build());                        
                        objlist.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(objlist);
        LOGGER.debug("ModifyParamOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
