/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultIdType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultServType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdDateTime;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getfaultoutput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getfaultoutput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getfaultoutput.obj.FaultObj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getfaultoutput.obj.FaultObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates GetFaultOutput messages (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/*
<!-- Example: Fault Reporting Response -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>34650</msgUID>
    </header>
    <body>
        <getFaultResp>
            <result>SUCCESS</result>
            <obj objID="RE:0">
                <fault>
                    <faultID>FAULT_RE_OVERTEMP</faultID>
                    <severity>DEGRADED</severity>
                    <timestamp>2012-02-12T16:35:00Z</timestamp>
                    <descr>PA temp too high; Pout reduced</descr>
                    <affectedObj>TxSigPath_EUTRA:0</affectedObj>
                    <affectedObj>TxSigPath_EUTRA:1</affectedObj>
                </fault>
                <fault>
                    <faultID>FAULT_VSWR_OUTOF_RANGE</faultID>
                    <severity>WARNING</severity>
                    <timestamp>2012-02-12T16:01:05Z</timestamp>
                </fault>
            </obj>
        </getFaultResp>
    </body>
</msg>
*/

public class GetFaultOutputFactory implements OCPDeserializer<GetFaultOutput> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetFaultOutputFactory.class);
    private String faultTag = "fault";
    
    @Override
    public GetFaultOutput deserialize(List<Object> rawMessage) {
        GetFaultOutputBuilder builder = new GetFaultOutputBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        FaultObjBuilder faultobjbuilder = new FaultObjBuilder();

        List<FaultObj> faultobj = new ArrayList();
        List<Obj> obj = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("GetFaultOutputFactory - itr = " + tok);
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
                        LOGGER.trace("GetFaultOutputFactory - getMsgType = " + builder.getMsgType());
                    }  
                	//msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object uidtok = itr.next();
                        int uid = Integer.parseInt(((XmlCharacters)uidtok).data().toString());
                        builder.setXid((long)uid);
                        LOGGER.trace("GetFaultOutputFactory - getXid: " + builder.getXid());
                    }
                    //result
                    else if (((XmlElementStart)tok).name().equals("result")){
                        String rel = ((XmlCharacters)itr.next()).data().replace("_", "").toString();
                        builder.setResult(GetFaultRes.valueOf(rel));
                        LOGGER.trace("GetFaultOutputFactory - getResult: " + builder.getResult());
                    }
                    //obj
                    else if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                        objbuilder.setId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        LOGGER.trace("GetFaultOutputFactory - objbuilder getId: " + objbuilder.getId());

                         //find the token is XmlElementEnd:obj or XmlElementStart:fault obj.
                    	Object faulttok = itr.next();
                        while(!(faulttok instanceof XmlElementStart)){
                            if((faulttok instanceof XmlElementEnd) && ((XmlElementEnd)faulttok).name().equals("obj")){
                                break;
                            }
                            faulttok = itr.next();
                        }

                	    if(faulttok instanceof XmlElementStart) {
                	    	
                            while( !(((XmlElementStart)faulttok).name().equals("obj")) ) {
                            	if (((XmlElementStart)faulttok).name().equals(faultTag)) {
                            	    //find the token is XmlElementEnd:fault(No fault case) or XmlElementStart:faultID, ...faultItem.
                            	    faulttok = itr.next();
                                    while(!(faulttok instanceof XmlElementStart)){
                                        if((faulttok instanceof XmlElementEnd) && ((XmlElementEnd)faulttok).name().equals(faultTag)){
                                            break;
                                        }
                                        faulttok = itr.next();
                                    }

                            	    if(faulttok instanceof XmlElementStart) {
        	                            List affecttok = new ArrayList<String>();
        	                            while( !(((XmlElementStart)faulttok).name().equals(faultTag)) ) {
        	                            	if(((XmlElementStart)faulttok).name().equals("faultID")) {
                                  		        //get character(content)  
        	                            	    faulttok = itr.next();   
        	                            	    StringBuilder buf = new StringBuilder();
        	                                	while(faulttok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)faulttok).data().toString());
        	                                		faulttok = itr.next();
        	                                	}
        	                                	faultobjbuilder.setFaultID(FaultIdType.valueOf(buf.toString().replace("_", "")));
        	                                }
        	                            	else if(((XmlElementStart)faulttok).name().equals("severity")) {
                                  		        //get character(content)  
        	                            	    faulttok = itr.next();   
        	                            	    StringBuilder buf = new StringBuilder();
        	                                	while(faulttok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)faulttok).data().toString());
        	                                		faulttok = itr.next();
        	                                	}
        	                                	faultobjbuilder.setSeverity(FaultServType.valueOf(buf.toString()));
        	                                }
        	                            	else if(((XmlElementStart)faulttok).name().equals("timestamp")) {
                                  		        //get character(content)  
        	                            	    faulttok = itr.next();   
        	                            	    StringBuilder buf = new StringBuilder();
        	                                	while(faulttok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)faulttok).data().toString());
        	                                		faulttok = itr.next();
        	                                	}
        	                                	faultobjbuilder.setTimestamp(new XsdDateTime(buf.toString()));
        	                                }
        	                            	else if(((XmlElementStart)faulttok).name().equals("descr")) {
                                  		        //get character(content)  
        	                            	    faulttok = itr.next();   
        	                            	    StringBuilder buf = new StringBuilder();
        	                                	while(faulttok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)faulttok).data().toString());
        	                                		faulttok = itr.next();
        	                                	}
        	                                	faultobjbuilder.setDescr(buf.toString());
        	                            	}
        	                            	else if(((XmlElementStart)faulttok).name().equals("affectedObj")) {
                                  		        //get character(content)  
        	                            	    faulttok = itr.next();   
        	                            	    StringBuilder buf = new StringBuilder();
        	                                	while(faulttok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)faulttok).data().toString());
        	                                		faulttok = itr.next();
        	                                	}
        	                                	affecttok.add(buf.toString());
                                            	faultobjbuilder.setAffectedObj(affecttok);
        	                            	}
        	                            	else {
                                                //ignore non-fault parameter, jump to next fault/non-fault parameter
        	                            	    faulttok = itr.next();   
        	                                	while(faulttok instanceof XmlCharacters) {
        	                                	    faulttok = itr.next();
        	                                	}
        	                            	}
        	                            	        	                            	
        	                            	//find the token is XmlElementEnd:fault(end of fault case) or next XmlElementStart:faultID, ...faultItem.
        	                            	faulttok = itr.next();
        	                                while(!(faulttok instanceof XmlElementStart)){
        	                                    //find the token is XmlElementEnd:fault(end of fault case)
        	                                	if((faulttok instanceof XmlElementEnd) && ((XmlElementEnd)faulttok).name().equals(faultTag)){
                                                    break;
                                                }
        	                                	faulttok = itr.next();
        	                                }

                                            //find if XmlElementEnd:faultObj
                                            if (faulttok instanceof XmlElementEnd) {
        	                                    LOGGER.trace("GetFaultOutputFactory - found XmlElementEnd: {}", faulttok);
        	                                    if(((XmlElementEnd)faulttok).name().equals(faultTag)) {
        	                                    	//find the next token is XmlElementStart:faultObj or XmlElementEnd:obj
        	                                        faulttok = itr.next();
        	                                        while(faulttok instanceof XmlCharacters) {
        	                                            faulttok = itr.next();
        	                                        }
        	                                		break; //faultObj
        	                                    }
        	                                }
        	                            }
                            	    }
                            	    else if ((faulttok instanceof XmlElementEnd) && ((XmlElementEnd)faulttok).name().equals(faultTag)){
                                        LOGGER.info("GetFaultOutputFactory - No Fault happened");
                                	    faulttok = itr.next();
                                        while(!(faulttok instanceof XmlElementStart)){
                                            if((faulttok instanceof XmlElementEnd) && ((XmlElementEnd)faulttok).name().equals("obj")){
                                                break;
                                            }
                                            faulttok = itr.next();
                                        }
                            	    }
                                    
    	                            LOGGER.trace("GetFaultOutputFactory - faultobjbuilder = " + faultobjbuilder.build());
    	                            faultobj.add(faultobjbuilder.build());
    	                            faultobjbuilder = new FaultObjBuilder();
                            	}
                            	
                            	if (faulttok instanceof XmlElementEnd) {
                                    LOGGER.trace("GetFaultOutputFactory - found XmlElementEnd: {}", faulttok);
                                    if(((XmlElementEnd)faulttok).name().equals("obj")) {
                                	break;
                                    }
                                }
                            	
                            	//jump into next round of this while loop, make sure it instance of XmlElementStart
                                while(!(faulttok instanceof XmlElementStart)){
                                    faulttok = itr.next();
                                }
                            }
                	    }
                	    else if ((faulttok instanceof XmlElementEnd) && ((XmlElementEnd)faulttok).name().equals("obj")){
                    	    LOGGER.info("GetFaultOutputFactory - No fault obj return");
                        }
                        objbuilder.setFaultObj(faultobj);  
                        
                        LOGGER.trace("GetFaultOutputFactory - objbuilder getFaultObj = " + objbuilder.build());
                        obj.add(objbuilder.build());
                    } 
                }
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(obj);
        LOGGER.debug("GetFaultOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
