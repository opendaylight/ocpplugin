/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateVal;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyStateRes;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.utils.MessageHelper;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translates ModifyStateResp message (OCP Protocol v4.1.1)
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

/* limitation: objId:1, state(name, result):1 */

/*
<!-- Example: Object State Modification Response -->
<msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1">
    <header>
        <msgType>RESP</msgType>
        <msgUID>34570</msgUID>
    </header>
    <body>
        <modifyStateResp>
            <result>SUCCESS</result>
            <obj objID="exampleObj:0">
                <state type="AST">UNLOCKED</state>
            </obj>
        </modifyStateResp>
    </body>
</msg>
*/

public class ModifyStateOutputFactory implements OCPDeserializer<ModifyStateOutput> {
    private static final Logger LOG = LoggerFactory.getLogger(ModifyStateOutputFactory.class);

    @Override
    public ModifyStateOutput deserialize(List<Object> rawMessage) {
        ModifyStateOutputBuilder builder = new ModifyStateOutputBuilder();
        Iterator itr = rawMessage.iterator();
        
        while(itr.hasNext()) {
            Object tok = itr.next();
            LOG.trace("ModifyStateOutputFactory - itr = {}", tok);
            try {
                if(tok instanceof XmlElementStart) {
                    //msgType
                    if (((XmlElementStart)tok).name().equals("body")){
                        String type = MessageHelper.getMsgType(itr);
                        builder.setMsgType(OcpMsgType.valueOf(type));
                    }
                    //msgUID
                    if (((XmlElementStart)tok).name().equals("msgUID")){
                        String uidStr = MessageHelper.getMsgUID(itr);
                        int uid = Integer.parseInt(uidStr);
                        builder.setXid((long)uid);
                    }
                    //result
                    if (((XmlElementStart)tok).name().equals("result")){
                        String rel = MessageHelper.getResult(itr);
                        builder.setResult(ModifyStateRes.valueOf(rel));
                    }
                    //obj
                    if (((XmlElementStart)tok).name().equals("obj")) {
                        //set Obj ID
                        builder.setObjId(new ObjId(((XmlElementStart)tok).attributes().get(0).value()));
                        LOG.debug("ModifyStateOutputFactory - builder getObjId = {}", builder.getObjId());

                        Object objtok = itr.next();
                        while(!(objtok instanceof XmlElementStart)){
                            objtok = itr.next();
                        }
                        
                        while(objtok instanceof XmlElementStart) {
                            if(((XmlElementStart)objtok).name().equals("state")) {
                                //set state Name                       
                                String tmp = ((XmlElementStart)objtok).attributes().get(0).value();                                
                                builder.setStateType(StateType.valueOf(tmp));
                                LOG.debug("ModifyStateOutputFactory - builder getStateType = {}", builder.getStateType());

                                //set state Value
                                String bufStr = MessageHelper.getCharVal(itr);
                                builder.setStateValue(StateVal.valueOf(bufStr));
                                LOG.debug("ModifyStateOutputFactory - builder getStateValue = {}", builder.getStateValue());

                                //skip state of XmlElementEnd
                                while(!(objtok instanceof XmlElementEnd)){
                                    objtok = itr.next();                            
                                }
                                
                                //skip remain of XmlCharacters
                                objtok = itr.next();
                                while((objtok instanceof XmlCharacters)){
                                    objtok = itr.next(); 
                                }
                                LOG.debug("ModifyStateOutputFactory - found next: {}", objtok);
                            
                            }
                            
                            if (objtok instanceof XmlElementEnd) {
                                LOG.debug("ModifyStateOutputFactory - found XmlElementEnd: {}", objtok);
                                if(((XmlElementEnd)objtok).name().equals("obj")) {
                                    LOG.debug("ModifyStateOutputFactory - objtok20 = {}", objtok);
                                }
                            }
                            else{
                                while(!(objtok instanceof XmlElementStart)){
                                    LOG.debug("ModifyStateOutputFactory - objtok  xx = {}", objtok);
                                    objtok = itr.next();
                                }
                            }
                        }
                    }
                } 
            }
            catch( Exception t ) {
                LOG.error("Error {} {}", tok, t.toString());
            }
        }
        LOG.debug("ModifyStateOutputFactory - Builder: {}", builder.build());
        return builder.build();
    }
}
