/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.clients;

import java.util.ArrayDeque;
import java.util.Deque;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.*;

import java.util.*;
import org.opendaylight.ocpjava.util.ByteBufUtils;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.Param;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for providing prepared handshake scenario
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public final class ScenarioFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioFactory.class);
    private ScenarioFactory() {
        throw new UnsupportedOperationException("Utility class shouldn't be instantiated");
    }

    /**
     * Creates stack with handshake needed messages. XID of messages:
     * <ol>
     *   <li> helloInd sent
     *   <li> helloAck waiting
     *   <li> setTimeReq waiting
     *   <li> setTimeResp sent
     *   <li> getParamReq waiting
     *   <li> getParamResp sent
     * </ol>
     * @return stack filled with Handshake messages
     */
    
    public static Deque<ClientEvent> createHandshakeScenario() {
        Deque<ClientEvent> stack = new ArrayDeque<>();
        
        StringBuilder seq = new StringBuilder("");
        seq.append("<msg xmlns=");
        seq.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq.append("<header>");
                seq.append("<msgType>IND</msgType>");
                seq.append("<msgUID>0</msgUID>");
            seq.append("</header>");              
            seq.append("<body>");
                seq.append("<helloInd>");
                    seq.append("<version>4.1.1</version>");
                    seq.append("<vendorId>MTI</vendorId>");
                    seq.append("<serialNumber>101-3333</serialNumber>");
                seq.append("</helloInd>");
            seq.append("</body>");
        seq.append("</msg>");
        
        byte[] bytesData = String.valueOf(seq).getBytes();
        stack.addFirst(new SendEvent(bytesData));
        
        
        StringBuilder seq2 = new StringBuilder("");
        seq2.append("<msg xmlns=");
        seq2.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq2.append("<header>");
                seq2.append("<msgType>ACK</msgType>");
                seq2.append("<msgUID>0</msgUID>");
            seq2.append("</header>");       
            seq2.append("<body>");
                seq2.append("<helloAck>");
                    seq2.append("<result>SUCCESS</result>");
                seq2.append("</helloAck>");
            seq2.append("</body>");
        seq2.append("</msg>");
        
        byte[] bytesData2 = String.valueOf(seq2).getBytes();
        
        try{
            String buf2 = new String(bytesData2, "UTF-8");
            LOGGER.debug("<< {}", buf2);
        }catch(Exception e){
            e.printStackTrace();
        }
        stack.addFirst(new WaitForMessageEvent(bytesData2));

        
        StringBuilder seq5 = new StringBuilder("");
        seq5.append("<msg xmlns=");
        seq5.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq5.append("<header>");
                seq5.append("<msgType>REQ</msgType>");
                seq5.append("<msgUID>1</msgUID>");
            seq5.append("</header>");              
            seq5.append("<body>");
                seq5.append("<"); seq5.append("setTimeReq"); seq5.append(">");
                    seq5.append("<newTime>2012-04-26T10:23:00-05:00</newTime>");
                seq5.append("</"); seq5.append("setTimeReq"); seq5.append(">");
            seq5.append("</body>");
        seq5.append("</msg>");
        
        byte[] bytesData5 = String.valueOf(seq5).getBytes();
        
        try{
            String buf5 = new String(bytesData5, "UTF-8");
            LOGGER.debug("<< {}", buf5);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        stack.addFirst(new WaitForMessageEvent(bytesData5));
        

        StringBuilder seq6 = new StringBuilder("");
        seq6.append("<msg xmlns=");
        seq6.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq6.append("<header>");
                seq6.append("<msgType>RESP</msgType>");
                seq6.append("<msgUID>1</msgUID>");
            seq6.append("</header>");              
            seq6.append("<body>");
                seq6.append("<"); seq6.append("setTimeResp"); seq6.append(">");
                    seq6.append("<result>SUCCESS</result>");
                seq6.append("</"); seq6.append("setTimeResp"); seq6.append(">");
            seq6.append("</body>");
        seq6.append("</msg>");
        
        byte[] bytesData6 = String.valueOf(seq6).getBytes();
        stack.addFirst(new SendEvent(bytesData6));
        
        
        StringBuilder seq3 = new StringBuilder("");
        seq3.append("<msg xmlns=");
        seq3.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq3.append("<header>");
                seq3.append("<msgType>REQ</msgType>");
                seq3.append("<msgUID>"); seq3.append("2"); seq3.append("</msgUID>");
            seq3.append("</header>");
            seq3.append("<body>");
                seq3.append("<"); seq3.append("getParamReq"); seq3.append(">");
                    seq3.append("<obj objID=\"");
                    seq3.append("ALL");
                    seq3.append("\">");               
                       seq3.append("<param name=\"");
                       seq3.append("ALL");
                       seq3.append("\"/>");
                    seq3.append("</obj>");
                seq3.append("</"); seq3.append("getParamReq"); seq3.append(">");
            seq3.append("</body>");
        seq3.append("</msg>");
        
        byte[] bytesData3 = String.valueOf(seq3).getBytes();
        stack.addFirst(new WaitForMessageEvent(bytesData3));

        
        StringBuilder seq4 = new StringBuilder("");
        seq4.append("<msg xmlns=");
        seq4.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
            seq4.append("<header>");
                seq4.append("<msgType>RESP</msgType>");
                seq4.append("<msgUID>"); seq4.append("2"); seq4.append("</msgUID>");
            seq4.append("</header>");
            seq4.append("<body>");
                seq4.append("<"); seq4.append("getParamResp"); seq4.append(">");
                    seq4.append("<result>SUCCESS</result>");
                    seq4.append("<obj objID=\"");
                    seq4.append("ALL");
                    seq4.append("\">");
                        seq4.append("<param name=\"vendorID\">xyz</param>");
                        seq4.append("<param name=\"productID\">acme0815</param>");
                        seq4.append("<param name=\"productRev\">A.01</param>");
                    seq4.append("</obj>");
                seq4.append("</"); seq4.append("getParamResp"); seq4.append(">");
            seq4.append("</body>");
        seq4.append("</msg>");
        
        byte[] bytesData4 = String.valueOf(seq4).getBytes();
        stack.addFirst(new SendEvent(bytesData4));

        return stack;
    }
}
