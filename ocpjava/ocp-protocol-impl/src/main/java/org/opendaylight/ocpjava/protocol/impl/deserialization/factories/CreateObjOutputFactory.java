/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;
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
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.utils.MessageHelper;
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
        List<Obj> objs = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("CreateObjOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                    //msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        String type = MessageHelper.getMsgType(itr);
                        builder.setMsgType(OcpMsgType.valueOf(type));
                    }
                	//msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        String uidStr = MessageHelper.getMsgUID(itr);
                        int uid = Integer.parseInt(uidStr);
                        builder.setXid((long)uid);
                    }
                    //globResult
                    else if (((XmlElementStart)tok).name().equals("globResult")){
                        String gloRel = MessageHelper.getResult(itr);
                        builder.setGlobResult(CreateObjGlobRes.valueOf(gloRel));
                    }
                    //obj
                    else if (((XmlElementStart)tok).name().equals("obj")) {
                    	//set Obj ID
                    	if(((XmlElementStart)tok).attributes().size() >= 1){
                    	    objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                    	}
                    	objbuilder.setParam(getListParam(itr));
              
                        LOGGER.trace("CreateObjOutputFactory - objbuilder.build() " + objbuilder.build());
                        objs.add(objbuilder.build());
                    }
                } 
            }
            catch( Exception t ) {
                LOGGER.error("Error {} ", tok, t);
            }
        }
        builder.setObj(objs);
        LOGGER.debug("CreateObjOutputFactory - Builder: " + builder.build());
        return builder.build();
    }


    public List<Param> getListParam(Iterator itr){
        
        ParamBuilder parambuilder = new ParamBuilder();
        List<Param> plist = new ArrayList();

        //find the next param of XmlElementStart
        Object tok = findNextParamObj(itr);
        if (tok instanceof XmlElementStart) {
            while(!(((XmlElementStart)tok).name().equals("obj"))){
                if(((XmlElementStart)tok).name().equals("param")) {
                    parambuilder.setName(((XmlElementStart)tok).attributes().get(0).value());
                    
                    Object result = findNextResultObj(itr);
                    
                    if(((XmlElementStart)result).name().equals("result")) {
                        tok = itr.next();
                        String rel = "";
                        while(tok instanceof XmlCharacters){
                            rel = rel.concat(((XmlCharacters)tok).data().toString().replace("_", ""));
                            LOGGER.debug("CreateObjOutputFactory - result = {}", ((XmlCharacters)tok).data().toString().replace("_", ""));
                            tok = itr.next();
                        }
                        parambuilder.setResult(CreateObjRes.valueOf(rel));
                    }
                    else {
                        tok = itr.next();
                    }
                    
                    LOGGER.debug("CreateObjOutputFactory - parambuilder.build() " + parambuilder.build());
                    plist.add(parambuilder.build());
                    parambuilder = new ParamBuilder();
                    
                    while((tok instanceof XmlElementStart)||(tok instanceof XmlElementEnd)||(tok instanceof XmlCharacters)) {
                        if ((tok instanceof XmlElementStart) && ((XmlElementStart)tok).name().equals("param")){
                            break;
                        }
                        else if ((tok instanceof XmlElementEnd) && ((XmlElementEnd)tok).name().equals("obj")) {
                            break;
                        }
                        tok = itr.next();
                    }
                    
                    if ((tok instanceof XmlElementEnd) && ((XmlElementEnd)tok).name().equals("obj")) {
                        break;
                    }
                }
            }
        }                   
        return plist;
    }

    //find the next param of XmlElementStart
    public Object findNextParamObj(Iterator itr){
        Object tok = itr.next();  
        while(!(tok instanceof XmlElementStart)) {
            //if obj of XmlElementEnd found, it means there is no param in this object 
            if((tok instanceof XmlElementEnd) && ((XmlElementEnd)tok).name().equals("obj")){
                break;
            }
            tok = itr.next();
        }
        return tok;
    }

    //find the next result of XmlElementStart
    public Object findNextResultObj(Iterator itr){
        Object tok = itr.next();
        while(!(tok instanceof XmlElementStart)){
            tok = itr.next();
        }
        return tok;
    }
}
