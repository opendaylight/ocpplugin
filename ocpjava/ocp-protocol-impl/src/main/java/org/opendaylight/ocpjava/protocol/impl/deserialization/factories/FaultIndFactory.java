/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultIdType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.IndFaultState;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultServType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdDateTime;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultIndBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.faultindsource.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.faultindsource.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.faultindsource.obj.FaultObj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.faultindsource.obj.FaultObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.utils.MessageHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates FaultInd Message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Fault Reporting Indication -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>IND</msgType>
        <msgUID>0</msgUID>
    </header>
    <body>
        <faultInd>
            <obj objID="RE:0">
                <fault>
                    <faultID>FAULT_TX_GAIN_FAIL</faultID>
                    <state>ACTIVE</state>
                    <severity>FAILED</severity>
                    <descr>PA failure</descr>
                    <affectedObj>TxSigPath_UTRAFDD:0</affectedObj>
                </fault>
            </obj>
        </faultInd>
    </body>
</msg>
*/

public class FaultIndFactory implements OCPDeserializer<FaultInd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaultIndFactory.class);	
    private String faultTag = "fault";
    
    @Override
    public FaultInd deserialize(List<Object> rawMessage) {
        FaultIndBuilder builder = new FaultIndBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        FaultObjBuilder faultobjbuilder = new FaultObjBuilder();

        List<FaultObj> faultobj = new ArrayList();
        List<Obj> obj = new ArrayList();
     
        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("FaultIndFactory - itr = " + tok);
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
                    //obj
                    else if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                        objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        LOGGER.debug("FaultIndFactory - faultobjbuilder getId: " + objbuilder.getId());

                        //Character
                        tok = itr.next(); 
                        while(!(tok instanceof XmlElementStart)){
                        	tok = itr.next();
                        }
                        //fault
                        if (((XmlElementStart)tok).name().equals(faultTag)) {
                            //Character
                            itr.next();
                            //faultID elementStart
                            Object faultTok = itr.next();
                            List afftok = new ArrayList<String>();
                            while( !(((XmlElementStart)faultTok).name().equals(faultTag)) ) {
                                if(((XmlElementStart)faultTok).name().equals("faultID")) {
                            	    //get Character
                                    String bufStr = MessageHelper.getCharVal(itr);
                                    faultobjbuilder.setFaultID(FaultIdType.valueOf(bufStr));
                                }
                            	else if(((XmlElementStart)faultTok).name().equals("state")) {
                      		        //get state character(content)  
                            	    String bufStr = MessageHelper.getCharVal(itr);
                                    faultobjbuilder.setState(IndFaultState.valueOf(bufStr));
                                }
                            	else if(((XmlElementStart)faultTok).name().equals("severity")) {
                      	            //get severity character(content)  
                                    String bufStr = MessageHelper.getCharVal(itr);
                                    faultobjbuilder.setSeverity(FaultServType.valueOf(bufStr));
                                }
                            	else if(((XmlElementStart)faultTok).name().equals("timestamp")) {
                      		        //get timestamp character(content)  
                                    String bufStr = MessageHelper.getCharVal(itr);
                                    faultobjbuilder.setTimestamp(new XsdDateTime(bufStr));
                                }
                            	else if(((XmlElementStart)faultTok).name().equals("descr")) {
                                    //get descr character(content)  
                            	    faultTok = itr.next();   
                                    StringBuilder buf = new StringBuilder();
                                    while(faultTok instanceof XmlCharacters) {
                                        buf.append(((XmlCharacters)faultTok).data().toString());
                                        faultTok = itr.next();
                                    }
                                    faultobjbuilder.setDescr(buf.toString());
                                }
                            	else if(((XmlElementStart)faultTok).name().equals("affectedObj")) {
                                    //get descr character(content)  
                                    String bufStr = MessageHelper.getCharVal(itr);
                                    afftok.add(bufStr);
                                    faultobjbuilder.setAffectedObj(afftok);
                                }
                            	else {
                                    //ignore non-fault parameter, jump to next fault/non-fault parameter
                            	    faultTok = itr.next();   
                                    while(faultTok instanceof XmlCharacters) {
                                        faultTok = itr.next();
                                    }
                                }
                            	
                                faultTok = itr.next(); 
                                while(!(faultTok instanceof XmlElementStart)) {
                                    if ((faultTok instanceof XmlElementEnd) && ((XmlElementEnd)faultTok).name().equals(faultTag)) {
                                        break;
                                    }
                                    faultTok = itr.next();
                                }

                                if ((faultTok instanceof XmlElementEnd) && ((XmlElementEnd)faultTok).name().equals(faultTag)) {
                                    break;
                                }
                            }
                            faultobjbuilder.setAffectedObj(afftok);
                            LOGGER.trace("FaultIndFactory - faultobjbuilder: " + faultobjbuilder.build());
                            faultobj.add(faultobjbuilder.build());
                        }
                        objbuilder.setFaultObj(faultobj);                    
                        obj.add(objbuilder.build());
                        LOGGER.trace("FaultIndFactory - objbuilder: " + objbuilder.build());
                    } 
                }
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(obj);
        LOGGER.debug("FaultIndFactory - Builder: " + builder.build());
        return builder.build();
    }
}
