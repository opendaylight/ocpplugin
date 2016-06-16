/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateVal;
//import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.statechangesource.obj.State;
//import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.statechangesource.obj.StateBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateoutput.obj.State;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateoutput.obj.StateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

public abstract class MessageHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHelper.class);

    //get the msgType value
    public static String getMsgType(Iterator itr){
        Object tok = itr.next();
        while(!(tok instanceof XmlElementStart)){
            tok = itr.next();
        }
        String type = ((XmlElementStart)tok).name().toUpperCase();
        LOGGER.debug("MessageHelper - getMsgType = {}", type);
        return type;
    }
    
    //get, combine the msgUID value if needed, getChar
    public static String getMsgUID(Iterator itr){
        Object tok = itr.next();
        String rel = "";
        while(tok instanceof XmlCharacters){
            rel = rel.concat(((XmlCharacters)tok).data().toString().replace(" ", "").replace("\n", ""));
            tok = itr.next();
        }
        LOGGER.debug("MessageHelper - getMsgUID = {}", rel);
        return rel;
    }

    //get, combine the result value, getCharNoUnder
    public static String getResult(Iterator itr){
        Object tok = itr.next();
        String rel = "";
        while(tok instanceof XmlCharacters){
            rel = rel.concat(((XmlCharacters)tok).data().toString().replace("_", "").replace(" ", "").replace("\n", ""));
            tok = itr.next();
        }
        LOGGER.debug("MessageHelper - getResult = {}", rel);
        return rel;
    }

    //get, combine the character values if needed
    public static String getCharVal(Iterator itr){
        Object tok = itr.next();
        String rel = "";
        while(tok instanceof XmlCharacters){
            rel = rel.concat(((XmlCharacters)tok).data().toString().replace("_", "").replace(" ", "").replace("\n", ""));
            tok = itr.next();
        }
        LOGGER.debug("MessageHelper - getCharVal = {}", rel);
        return rel;
    }
}


