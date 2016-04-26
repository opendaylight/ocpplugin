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
                        //XmlCharacters of body
                        itr.next(); 
                        //XmlElementStart of msgType
                    	Object t_tok = itr.next(); 
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

                        //Character
                        tok = itr.next(); 
                        while(!(tok instanceof XmlElementStart))
                        	tok = itr.next();
                        //fault
                        if (((XmlElementStart)tok).name().equals("fault")) {
                            //Character
                            itr.next();
                            //faultID elementStart
                            Object faulttok = itr.next();
                            List afftok = new ArrayList<String>();
                            while( !(((XmlElementStart)faulttok).name().equals("fault")) ) {
                                if(((XmlElementStart)faulttok).name().equals("faultID")) {
                            	    //get Character
                            	    faulttok = itr.next();
                                    if(faulttok instanceof XmlCharacters) {
                                    	faultobjbuilder.setFaultID(FaultIdType.valueOf(((XmlCharacters)faulttok).data().toString().replace("_", "")));
                                        LOGGER.debug("FaultIndFactory - faultobjbuilder getFaultID: " + faultobjbuilder.getFaultID());
                            	    }
                                }
                            	else if(((XmlElementStart)faulttok).name().equals("state")) {
                      		    //get state character(content)  
                            	    faulttok = itr.next();   
                                    StringBuilder buf = new StringBuilder();
                                    while(faulttok instanceof XmlCharacters) {
                                        buf.append(((XmlCharacters)faulttok).data().toString());
                                        faulttok = itr.next();
                                    }
                                    faultobjbuilder.setState(IndFaultState.valueOf(buf.toString()));
                                }
                            	else if(((XmlElementStart)faulttok).name().equals("severity")) {
                      	            //get severity character(content)  
                            	    faulttok = itr.next();   
                                    StringBuilder buf = new StringBuilder();
                                    while(faulttok instanceof XmlCharacters) {
                                        buf.append(((XmlCharacters)faulttok).data().toString());
                                        faulttok = itr.next();
                                    }
                                    faultobjbuilder.setSeverity(FaultServType.valueOf(buf.toString()));
                                }
                            	else if(((XmlElementStart)faulttok).name().equals("timestamp")) {
                      		    //get timestamp character(content)  
                            	    faulttok = itr.next();   
                                    StringBuilder buf = new StringBuilder();
                                    while(faulttok instanceof XmlCharacters) {
                                        buf.append(((XmlCharacters)faulttok).data().toString());
                                        faulttok = itr.next();
                                    }
                                    faultobjbuilder.setTimestamp(new XsdDateTime(buf.toString()));
                                }
                            	else if(((XmlElementStart)faulttok).name().equals("descr")) {
                                    //get descr character(content)  
                            	    faulttok = itr.next();   
                                    StringBuilder buf = new StringBuilder();
                                    while(faulttok instanceof XmlCharacters) {
                                        buf.append(((XmlCharacters)faulttok).data().toString());
                                        faulttok = itr.next();
                                    }
                                    faultobjbuilder.setDescr(buf.toString());
                                }
                            	else if(((XmlElementStart)faulttok).name().equals("affectedObj")) {
                                    //get descr character(content)  
                            	    faulttok = itr.next();   
                                    StringBuilder buf = new StringBuilder();
                                    while(faulttok instanceof XmlCharacters) {
                                        buf.append(((XmlCharacters)faulttok).data().toString());
                                        faulttok = itr.next();
                                    }
                                    afftok.add(buf.toString());
                                    faultobjbuilder.setAffectedObj(afftok);
                                }
                            	else {
                                    //ignore non-fault parameter, jump to next fault/non-fault parameter
                            	    faulttok = itr.next();   
                                    while(faulttok instanceof XmlCharacters) {
                                        faulttok = itr.next();
                                    }
                                }
                            	
                            	faulttok = itr.next(); 
                                while(!(faulttok instanceof XmlElementStart)) {
                                    if (faulttok instanceof XmlElementEnd) {
                                        if(((XmlElementEnd)faulttok).name().equals("fault"))
                                            break;
                                    }
                                    faulttok = itr.next();
                                }

                                if (faulttok instanceof XmlElementEnd) {
                                    if(((XmlElementEnd)faulttok).name().equals("fault"))
                                        break;
                                }
                            }
                            faultobjbuilder.setAffectedObj(afftok);
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
