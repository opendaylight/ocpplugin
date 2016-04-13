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
    @Override
    public GetFaultOutput deserialize(List<Object> rawMessage) {
        GetFaultOutputBuilder builder = new GetFaultOutputBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        FaultObjBuilder faultobjbuilder = new FaultObjBuilder();

        List<FaultObj> llist_faultobj = new ArrayList();
        List<Obj> llist_obj = new ArrayList();

        Iterator itr = rawMessage.iterator();
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOGGER.trace("GetFaultOutputFactory - itr = " + tok);
            try {
                if(tok instanceof XmlElementStart) {
                	//msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        itr.next(); //XmlCharacters of body
                    	Object t_tok = itr.next(); // XmlElementStart of msgType
                        if (t_tok instanceof XmlElementStart)
                    	    builder.setMsgType(OcpMsgType.valueOf(((XmlElementStart)t_tok).name().toUpperCase()));
                        LOGGER.trace("GetFaultOutputFactory - getMsgType = " + builder.getMsgType());
                    }  
                	//msgUID
                    else if (((XmlElementStart)tok).name().equals("msgUID")){
                        Object t_tok = itr.next();
                        int uid_tok = Integer.parseInt(((XmlCharacters)t_tok).data().toString());
                        builder.setXid((long)uid_tok);
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
                    	Object f_tok = itr.next();
                        while(!(f_tok instanceof XmlElementStart)){
                            if(f_tok instanceof XmlElementEnd)
                            	if(((XmlElementEnd)f_tok).name().equals("obj")) {
                            	    break;
                            	}
                        	f_tok = itr.next();
                        }

                	    if(f_tok instanceof XmlElementStart) {
                	    	
                            while( !(((XmlElementStart)f_tok).name().equals("obj")) ) {
                            	if (((XmlElementStart)f_tok).name().equals("fault")) {
                            		
                            	    //find the token is XmlElementEnd:fault(No fault case) or XmlElementStart:faultID, ...faultItem.
                                    f_tok = itr.next();
                                    while(!(f_tok instanceof XmlElementStart)){
                                        if(f_tok instanceof XmlElementEnd)
                                            if(((XmlElementEnd)f_tok).name().equals("fault")) {
                                        	break;
                                            }
                                    	f_tok = itr.next();
                                    }

                            	    if(f_tok instanceof XmlElementStart) {
        	                            List aff_tok = new ArrayList<String>();
        	                            while( !(((XmlElementStart)f_tok).name().equals("fault")) ) {
        	                            	if(((XmlElementStart)f_tok).name().equals("faultID")) {

                                  		        //get character(content)  
        	                            	    f_tok = itr.next();   
        	                                	StringBuffer buf = new StringBuffer();
        	                                	while(f_tok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)f_tok).data().toString());
        	                                		f_tok = itr.next();
        	                                	}
        	                                	faultobjbuilder.setFaultID(FaultIdType.valueOf(buf.toString().replace("_", "")));
        	                                }
        	                            	else if(((XmlElementStart)f_tok).name().equals("severity")) {

                                  		        //get character(content)  
        	                            	    f_tok = itr.next();   
        	                                	StringBuffer buf = new StringBuffer();
        	                                	while(f_tok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)f_tok).data().toString());
        	                                		f_tok = itr.next();
        	                                	}
        	                                	faultobjbuilder.setSeverity(FaultServType.valueOf(buf.toString()));
        	                                }
        	                            	else if(((XmlElementStart)f_tok).name().equals("timestamp")) {

                                  		        //get character(content)  
        	                            	    f_tok = itr.next();   
        	                                	StringBuffer buf = new StringBuffer();
        	                                	while(f_tok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)f_tok).data().toString());
        	                                		f_tok = itr.next();
        	                                	}
        	                                	faultobjbuilder.setTimestamp(new XsdDateTime(buf.toString()));
        	                                }
        	                            	else if(((XmlElementStart)f_tok).name().equals("descr")) {
        	                            	    
                                  		        //get character(content)  
        	                            	    f_tok = itr.next();   
        	                                	StringBuffer buf = new StringBuffer();
        	                                	while(f_tok instanceof XmlCharacters) {
        	                                		buf.append(((XmlCharacters)f_tok).data().toString());
        	                                		f_tok = itr.next();
        	                                	}
        	                                	faultobjbuilder.setDescr(buf.toString());
        	                            	}
        	                            	else if(((XmlElementStart)f_tok).name().equals("affectedObj")) {
        	                            		
                                  		        //get character(content)  
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
        	                            	        	                            	
        	                            	//find the token is XmlElementEnd:fault(end of fault case) or next XmlElementStart:faultID, ...faultItem.
        	                                f_tok = itr.next();
        	                                while(!(f_tok instanceof XmlElementStart)){
        	                                    //find the token is XmlElementEnd:fault(end of fault case)
        	                                	if(f_tok instanceof XmlElementEnd)
                                                    if(((XmlElementEnd)f_tok).name().equals("fault")) {
                                                        break;
                                                    }
                                                f_tok = itr.next();
        	                                }

                                            //find if XmlElementEnd:faultObj
                                            if (f_tok instanceof XmlElementEnd) {
        	                                    LOGGER.trace("GetFaultOutputFactory - found XmlElementEnd: {}", f_tok);
        	                                    if(((XmlElementEnd)f_tok).name().equals("fault")) {
                                                    
        	                                    	//find the next token is XmlElementStart:faultObj or XmlElementEnd:obj
        	                                    	f_tok = itr.next();
        	                                        while(f_tok instanceof XmlCharacters) {
        	                                            f_tok = itr.next();
        	                                        }
        	                                		break; //faultObj
        	                                    }
        	                                }
        	                            }
                            	    }
                            	    else if (f_tok instanceof XmlElementEnd){
                                    	if(((XmlElementEnd)f_tok).name().equals("fault")) {
                                	    	LOGGER.debug("GetFaultOutputFactory - No Fault happened");
                                        	f_tok = itr.next();
                                            while(!(f_tok instanceof XmlElementStart)){
                                                if(f_tok instanceof XmlElementEnd)
                                                	if(((XmlElementEnd)f_tok).name().equals("obj")) {
                                                	    break;
                                                	}
                                            	f_tok = itr.next();
                                            }
                                    	}
                            	    }
                                    
    	                            LOGGER.trace("GetFaultOutputFactory - faultobjbuilder = " + faultobjbuilder.build());
    	                            llist_faultobj.add(faultobjbuilder.build());
    	                            faultobjbuilder = new FaultObjBuilder();
                            	}
                            	
                            	if (f_tok instanceof XmlElementEnd) {
                                    LOGGER.trace("GetFaultOutputFactory - found XmlElementEnd: {}", f_tok);
                                    if(((XmlElementEnd)f_tok).name().equals("obj")) {
                                	break;
                                    }
                                }
                            	
                            	//jump into next round of this while loop, make sure it instance of XmlElementStart
                                while(!(f_tok instanceof XmlElementStart))
                                	f_tok = itr.next();
                                }
                	    }
                	    else if (f_tok instanceof XmlElementEnd){
                            if(((XmlElementEnd)f_tok).name().equals("obj")) {
                    	        LOGGER.debug("GetFaultOutputFactory - No fault obj return");
                            }
                	}
                        objbuilder.setFaultObj(llist_faultobj);  
                        
                        LOGGER.trace("GetFaultOutputFactory - objbuilder getFaultObj = " + objbuilder.build());
                        llist_obj.add(objbuilder.build());
                    } 
                }
            }
            catch( Exception t ) {
                LOGGER.error("Error " + tok + " " + t.toString());
            }
        }
        builder.setObj(llist_obj);
        LOGGER.debug("GetFaultOutputFactory - Builder: " + builder.build());
        return builder.build();
    }
}
