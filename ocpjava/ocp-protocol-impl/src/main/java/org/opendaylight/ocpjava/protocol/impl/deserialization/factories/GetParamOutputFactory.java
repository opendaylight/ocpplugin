/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

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

        List<Param> paramlist = new ArrayList();
        List<Obj> objlist = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("GetParamOutputFactory - itr = " + tok);
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
                        LOGGER.debug("GetParamOutputFactory - getMsgType = " + builder.getMsgType());
                    }  
                    //msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object uidtok = itr.next();
                        int uid = Integer.parseInt(((XmlCharacters)uidtok).data().toString());
                        builder.setXid((long)uid);
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
                    	if(((XmlElementStart)tok).attributes().size() >= 1){
                            objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        }
                        LOGGER.debug("GetParamOutputFactory - objbuilder getId = " + objbuilder.getId());

                        Object objtok = itr.next();
                        while(!(objtok instanceof XmlElementStart)){
                            objtok = itr.next();
                        }

                        while( !(((XmlElementStart)objtok).name().equals("obj")) ) {
                            if(((XmlElementStart)objtok).name().equals("param")) {
                                //set param Name 
                            	if (((XmlElementStart)objtok).attributes().size() >= 1){
                                    parambuilder.setName(((XmlElementStart)objtok).attributes().get(0).value());
                                }
                                LOGGER.debug("GetParamOutputFactory - parambuilder getName = " + parambuilder.getName());
                                
                                //get param character(content)
                            	Object chartok = itr.next();      
                            	StringBuilder buf = new StringBuilder();
                            	while(chartok instanceof XmlCharacters) {
                            		buf.append(((XmlCharacters)chartok).data().toString());
                            		chartok = itr.next();
                            	}
                            	parambuilder.setValue(buf.toString());
                                
                                LOGGER.debug("GetParamOutputFactory - parambuilder getValue = " + parambuilder.getValue());
                                paramlist.add(parambuilder.build());
                                parambuilder = new ParamBuilder();
                            }

                            //jump to the next token until the token is param XmlElementStart or obj XmlElementEnd
                            objtok = itr.next();
                            while((objtok instanceof XmlElementStart)||(objtok instanceof XmlElementEnd)||(objtok instanceof XmlCharacters)) {
                                if ((objtok instanceof XmlElementStart) && ((XmlElementStart)objtok).name().equals("param")) {
                                    break;
                                }
                                else if ((objtok instanceof XmlElementEnd) && ((XmlElementEnd)objtok).name().equals("obj")) {
                                    break;
                                }
                                objtok = itr.next();
                            }

                            if ((objtok instanceof XmlElementEnd) && ((XmlElementEnd)objtok).name().equals("obj")) {
                                break;
                            }
                        }
                        objbuilder.setParam(paramlist);                    
                        paramlist = new ArrayList();
                        LOGGER.trace("GetParamOutputFactory - objbuilder.build(): " + objbuilder.build());
                        objlist.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(objlist);
        LOGGER.debug("GetParamOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
