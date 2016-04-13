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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultRes;
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
    @Override
    public FaultInd deserialize(List<Object> rawMessage) {
        FaultIndBuilder builder = new FaultIndBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        FaultObjBuilder faultobjbuilder = new FaultObjBuilder();

        List<FaultObj> llist_faultobj = new ArrayList();
        List<Obj> llist_obj = new ArrayList();
     
        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("FaultIndFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.debug("FaultIndFactory - getMsgType = " + builder.getMsgType());
                    }  
                	//msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object t_tok = itr.next();
                        int uid_tok = Integer.parseInt(((XmlCharacters)t_tok).data().toString());
                        builder.setXid((long)uid_tok);
                        LOGGER.debug("FaultIndFactory - getXid " + builder.getXid());
                    }
                    //obj
                    else if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                        objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        LOGGER.debug("FaultIndFactory - faultobjbuilder getId: " + objbuilder.getId());

                        tok = itr.next(); //Character
                        while(!(tok instanceof XmlElementStart))
                        	tok = itr.next();
                         //fault
                        if (((XmlElementStart)tok).name().equals("fault")) {
                        	itr.next(); //Character
                        	Object f_tok = itr.next(); //faultID elementStart
                            List aff_tok = new ArrayList<String>();
                            while( !(((XmlElementStart)f_tok).name().equals("fault")) ) {
                            	if(((XmlElementStart)f_tok).name().equals("faultID")) {
                            	    f_tok = itr.next(); //get Character
                            		if(f_tok instanceof XmlCharacters) {
                                    	faultobjbuilder.setFaultID(FaultIdType.valueOf(((XmlCharacters)f_tok).data().toString().replace("_", "")));
                                        LOGGER.debug("FaultIndFactory - faultobjbuilder getFaultID: " + faultobjbuilder.getFaultID());
                            		}
                                }
                            	else if(((XmlElementStart)f_tok).name().equals("state")) {
                            		
                      		        //get state character(content)  
                            	    f_tok = itr.next();   
                                    StringBuffer buf = new StringBuffer();
                                    while(f_tok instanceof XmlCharacters) {
                                		buf.append(((XmlCharacters)f_tok).data().toString());
                                		f_tok = itr.next();
                                    }
                                    faultobjbuilder.setState(IndFaultState.valueOf(buf.toString()));
                                }
                            	else if(((XmlElementStart)f_tok).name().equals("severity")) {

                      		        //get severity character(content)  
                            	    f_tok = itr.next();   
                                    StringBuffer buf = new StringBuffer();
                                    while(f_tok instanceof XmlCharacters) {
                                		buf.append(((XmlCharacters)f_tok).data().toString());
                                		f_tok = itr.next();
                                    }
                                    faultobjbuilder.setSeverity(FaultServType.valueOf(buf.toString()));
                                }
                            	else if(((XmlElementStart)f_tok).name().equals("timestamp")) {
                            		
                      		        //get timestamp character(content)  
                            	    f_tok = itr.next();   
                                    StringBuffer buf = new StringBuffer();
                                    while(f_tok instanceof XmlCharacters) {
                                		buf.append(((XmlCharacters)f_tok).data().toString());
                                		f_tok = itr.next();
                                    }
                                    faultobjbuilder.setTimestamp(new XsdDateTime(buf.toString()));
                                }
                            	else if(((XmlElementStart)f_tok).name().equals("descr")) {
                            		
                      		        //get descr character(content)  
                            	    f_tok = itr.next();   
                                    StringBuffer buf = new StringBuffer();
                                    while(f_tok instanceof XmlCharacters) {
                                		buf.append(((XmlCharacters)f_tok).data().toString());
                                		f_tok = itr.next();
                                    }
                                    faultobjbuilder.setDescr(buf.toString());
                                }
                            	else if(((XmlElementStart)f_tok).name().equals("affectedObj")) {
                            		
                      		        //get descr character(content)  
                            	    f_tok = itr.next();   
                                    StringBuffer buf = new StringBuffer();
                                    while(f_tok instanceof XmlCharacters) {
                                		buf.append(((XmlCharacters)f_tok).data().toString());
                                		f_tok = itr.next();
                                    }
                                	aff_tok.add(buf.toString());
                                	faultobjbuilder.setAffectedObj(aff_tok);
                                }
                            	else {
                                    //ignore non-fault parameter, jump to next fault/non-fault parameter
                            	    f_tok = itr.next();   
                                	while(f_tok instanceof XmlCharacters) {
                                		f_tok = itr.next();
                                	}
                            	}
                            	
                            	f_tok = itr.next(); 
                                while(!(f_tok instanceof XmlElementStart)) {
                                    if (f_tok instanceof XmlElementEnd) {
                                    	if(((XmlElementEnd)f_tok).name().equals("fault"))
                                    		break;
                                    }
                                    f_tok = itr.next();
                                }

                                if (f_tok instanceof XmlElementEnd) {
                                	if(((XmlElementEnd)f_tok).name().equals("fault"))
                                		break;
                                }
                            }
                            faultobjbuilder.setAffectedObj(aff_tok);
                            LOGGER.trace("FaultIndFactory - faultobjbuilder: " + faultobjbuilder.build());
                            llist_faultobj.add(faultobjbuilder.build());
                        }
                        objbuilder.setFaultObj(llist_faultobj);                    
                        llist_obj.add(objbuilder.build());
                        LOGGER.trace("FaultIndFactory - objbuilder: " + objbuilder.build());
                    } 
                }
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(llist_obj);
        LOGGER.debug("FaultIndFactory - Builder: " + builder.build());
        return builder.build();
    }
}
