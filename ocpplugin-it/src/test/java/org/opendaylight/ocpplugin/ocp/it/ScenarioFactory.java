/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.ocp.it;

import java.util.ArrayDeque;
import java.util.Deque;

import org.opendaylight.ocpjava.protocol.impl.clients.ClientEvent;
import org.opendaylight.ocpjava.protocol.impl.clients.SendEvent;
import org.opendaylight.ocpjava.protocol.impl.clients.SleepEvent;
import org.opendaylight.ocpjava.protocol.impl.clients.WaitForMessageEvent;

/**
 * provisioning of most common scenarios used by testing of integration between OCPLibrary, OCPPlugin and MD-SAL
 */
public abstract class ScenarioFactory {

    /** default sleep time [ms] - at scenario end */
    public static final int DEFAULT_SCENARIO_SLEEP = 2000;

    /**
     * Creates stack with handshake needed messages.
     *   OCP messages:
     *     helloInd sent
     *     helloAck waiting
     *     setTimeReq waiting
     *     setTimeResp sent
     *     getParamReq waiting
     *     getParamResp sent
     * @param addSleep if true - then add final sleep #DEFAULT_SCENARIO_SLEEP
     * @return stack filled with Handshake messages
     */
    public static Deque<ClientEvent> createHandshakeScenario(String ocpVersion,
                                                             String vendorId,
                                                             String serialNumber,
                                                             boolean addSleep) {
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
        seq.append("<version>" + ocpVersion + "</version>");
        seq.append("<vendorId>" + vendorId + "</vendorId>");
        seq.append("<serialNumber>" + serialNumber + "</serialNumber>");
        seq.append("</helloInd>");
        seq.append("</body>");
        seq.append("</msg>");
        stack.addFirst(new SendEvent(String.valueOf(seq).getBytes()));
       
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
        stack.addFirst(new WaitForMessageEvent(String.valueOf(seq2).getBytes()));

        StringBuilder seq5 = new StringBuilder("");
        seq5.append("<msg xmlns=");
        seq5.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
        seq5.append("<header>");
        seq5.append("<msgType>REQ</msgType>");
        seq5.append("<msgUID>1</msgUID>");
        seq5.append("</header>");              
        seq5.append("<body>");
        seq5.append("<setTimeReq>");
        seq5.append("<newTime>2012-04-26T10:23:00-05:00</newTime>");
        seq5.append("</setTimeReq>");
        seq5.append("</body>");
        seq5.append("</msg>");
        stack.addFirst(new WaitForMessageEvent(String.valueOf(seq5).getBytes()));       
 
        StringBuilder seq6 = new StringBuilder("");
        seq6.append("<msg xmlns=");
        seq6.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
        seq6.append("<header>");
        seq6.append("<msgType>RESP</msgType>");
        seq6.append("<msgUID>1</msgUID>");
        seq6.append("</header>");              
        seq6.append("<body>");
        seq6.append("<setTimeResp>");
        seq6.append("<result>SUCCESS</result>");
        seq6.append("</setTimeResp>");
        seq6.append("</body>");
        seq6.append("</msg>");
        stack.addFirst(new SendEvent(String.valueOf(seq6).getBytes()));       
 
        StringBuilder seq3 = new StringBuilder("");
        seq3.append("<msg xmlns=");
        seq3.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
        seq3.append("<header>");
        seq3.append("<msgType>REQ</msgType>");
        seq3.append("<msgUID>2</msgUID>");
        seq3.append("</header>");
        seq3.append("<body>");
        seq3.append("<getParamReq>");
        seq3.append("<obj objID=\"ALL\">");
        seq3.append("<param name=\"ALL\"/>");
        seq3.append("</obj>");
        seq3.append("</getParamReq>");
        seq3.append("</body>");
        seq3.append("</msg>");
        stack.addFirst(new WaitForMessageEvent(String.valueOf(seq3).getBytes())); 
        
        StringBuilder seq4 = new StringBuilder("");
        seq4.append("<msg xmlns=");
        seq4.append("\"http://uri.etsi.org/ori/002-2/v4.1.1\">");
        seq4.append("<header>");
        seq4.append("<msgType>RESP</msgType>");
        seq4.append("<msgUID>2</msgUID>");
        seq4.append("</header>");
        seq4.append("<body>");
        seq4.append("<getParamResp>");
        seq4.append("<result>SUCCESS</result>");
        seq4.append("<obj objID=\"ALL\">");
        seq4.append("<param name=\"vendorID\">" + vendorId + "</param>");
        seq4.append("<param name=\"productID\">acme0815</param>");
        seq4.append("<param name=\"productRev\">A.01</param>");
        seq4.append("</obj>");
        seq4.append("</getParamResp>");
        seq4.append("</body>");
        seq4.append("</msg>");
        stack.addFirst(new SendEvent(String.valueOf(seq4).getBytes()));

        if (addSleep) {
            addSleep(stack);
        }

        return stack;
    }

    /**
     * @param stack
     */
    private static void addSleep(Deque<ClientEvent> stack) {
        stack.addFirst(new SleepEvent(DEFAULT_SCENARIO_SLEEP));
    }

}
