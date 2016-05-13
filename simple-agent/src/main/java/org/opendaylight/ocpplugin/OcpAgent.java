/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.*;

/*
 * Simple OCP Agent
 *
 * @author Richard Chien <richard.chien@foxconn.com>
 */
public class OcpAgent 
{
    private static final String MSG_HELLO_ACK = "helloAck";
    private static final String MSG_SET_TIME_REQ = "setTimeReq";
    private static final String MSG_HEALTH_CHECK_REQ = "healthCheckReq";
    private static final String MSG_RESET_REQ = "resetReq";
    private static final String MSG_GET_PARAM_REQ = "getParamReq";
    private static final String MSG_GET_FAULT_REQ = "getFaultReq";
    private static final String MSG_GET_STATE_REQ = "getStateReq";
    private static final String MSG_MOD_PARAM_REQ = "modifyParamReq";
    private static final String MSG_MOD_STATE_REQ = "modifyStateReq";
    private static final String MSG_CRE_OBJ_REQ = "createObjReq";
    private static final String MSG_DEL_OBJ_REQ = "deleteObjReq";

    private static final String ELM_MSG_UID = "msgUID";
    private static final String ELM_RESULT = "result";
    private static final String ELM_OBJECT = "obj";
    private static final String ELM_PARAMETER = "param";

    private DataOutputStream out;

    private String vendorId = "unknown";
    private String serialNumber = "unknown";

    private String uid;
    private String value;
    private String result;
    private String obj;
    private String param;

   
    public OcpAgent(String vendorId, String serialNumber) {
        this.vendorId = vendorId;
        this.serialNumber = serialNumber;
    }

    public void createConnection(String controllerIpAddr, int portNumber) {
        try {
            Socket socket = new Socket(controllerIpAddr, portNumber);
            out = new DataOutputStream(socket.getOutputStream());
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
      
            Thread.sleep(500);
            sendHello();
         
            String buf = "";
            while (true) {
                int count = is.available();
                if (count > 0) {
                    byte[] bs = new byte[count];
                    dis.read(bs);
                    buf += new String(bs);
                    int index = buf.indexOf("</msg>");
                    while (index != -1) {
                        String msg = buf.substring(0, index + 6);
                        parseDocument(msg);
                        if (index + 6 == buf.length()) {
                            buf = "";
                            break;
                        }
                        else {
                            buf = buf.substring(index + 6, buf.length());
                            index = buf.indexOf("</msg>");
                        }
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + controllerIpAddr);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + controllerIpAddr);
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Couldn't sleep for 500 ms");
            System.exit(1);
        }
    }

    private void parseDocument(String msg) {
        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(msg));
            //Now iteration
            while (reader.hasNext()) {
                parseEvent(reader);
                reader.next();
            }
            reader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void parseEvent(XMLStreamReader reader) {
        switch (reader.getEventType()) {
            case XMLStreamConstants.START_DOCUMENT:
                //System.out.println("Start of document");
                break;
            case XMLStreamConstants.START_ELEMENT:
                //System.out.println("Start element = " + reader.getLocalName());
                break;
            case XMLStreamConstants.CHARACTERS:
                int beginIndex = reader.getTextStart();
                int endIndex = reader.getTextLength();
                value = new String(reader.getTextCharacters(),
                        beginIndex,
                        endIndex).trim();
                //if (!value.equalsIgnoreCase(""))
                //    System.out.println("Value = " + value);
                break;
            case XMLStreamConstants.END_ELEMENT:
                //System.out.println("End element = " + reader.getLocalName());
                
                if (reader.getLocalName().equals(ELM_MSG_UID)) {
                    uid = value;
                }
                else if (reader.getLocalName().equals(ELM_RESULT)) {
                    result = value;
                }

                else if (reader.getLocalName().equals(MSG_HELLO_ACK)) {
                    System.out.println("\n\nhello ack received (result = " + result + ")");
                    if (!result.equals("SUCCESS"))
                        System.exit(1);  
                }
                else if (reader.getLocalName().equals(MSG_SET_TIME_REQ)) {
                    sendSetTimeResp();
                }
                else if (reader.getLocalName().equals(MSG_HEALTH_CHECK_REQ)) {
                    sendHealthCheckResp(); 
                }
                else if (reader.getLocalName().equals(MSG_RESET_REQ)) {
                    sendResetResp();
                }
                else if (reader.getLocalName().equals(MSG_GET_PARAM_REQ)) {
                    sendGetParamResp();
                }
                else if (reader.getLocalName().equals(MSG_MOD_PARAM_REQ)){
                    sendModifyParamResp();
                }
                else if (reader.getLocalName().equals(MSG_GET_FAULT_REQ)){
                    sendGetFaultResp();
                }
                else if (reader.getLocalName().equals(MSG_GET_STATE_REQ)){
                    sendGetStateResp();
                }
                else if (reader.getLocalName().equals(MSG_MOD_STATE_REQ)){
                    sendModifyStateResp();
                }
                else if (reader.getLocalName().equals(MSG_CRE_OBJ_REQ)){
                    sendCreateObjResp();
                }
                else if (reader.getLocalName().equals(MSG_DEL_OBJ_REQ)){
                    sendDeleteObjResp();
                }


                break;
            case XMLStreamConstants.COMMENT:
                if (reader.hasText())
                    System.out.print(reader.getText());
                break;
        }
    }

    private void sendHello() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>IND</msgType>" +
                     "<msgUID>0</msgUID>" +
                     "</header>" + 
                     "<body>" +
                     "<helloInd>" +
                     "<version>4.1.1</version>" +
                     "<vendorId>" + vendorId + "</vendorId>" +
                     "<serialNumber>" + serialNumber + "</serialNumber>" +
                     "</helloInd>" +
                     "</body>" +
                     "</msg>";
        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSetTimeResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<setTimeResp>" +
                     "<result>SUCCESS</result>" +
                     "</setTimeResp>" +
                     "</body>" +
                     "</msg>";
        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendHealthCheckResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<healthCheckResp>" +
                     "<result>SUCCESS</result>" +
                     "</healthCheckResp>" +
                     "</body>" +
                     "</msg>";
        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    private void sendResetResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<resetResp>" +
                     "<result>SUCCESS</result>" +
                     "</resetResp>" +
                     "</body>" +
                     "</msg>";

        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendGetParamResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<getParamResp>" +
                     "<result>SUCCESS</result>" +
                     "<obj objID=\"RE:0\">" +
                     "<param name=\"vendorID\">" + vendorId + "</param>" +
                     "<param name=\"productID\">EMU</param>" +
                     "<param name=\"productRev\">1.0</param>" +
                     "<param name=\"serialNumber\">" + serialNumber + "</param>" +
                     "<param name=\"protocolVer\">4.1.1</param>" +
                     "<param name=\"agcTargetLevCfgGran\">RE</param>" +
                     "<param name=\"agcSettlTimeCfgGran\">RE</param>" +
                     "<param name=\"agcSettlTimeCap\">1fff</param>" +
                     "<param name=\"numAntPort\">2</param>" +
                     "<param name=\"numDataLinks\">1</param>" +
                     "<param name=\"numSigPathPerAntenna\">0</param>" +
                     "<param name=\"tBD_Bandwith\">20</param>" +
                     "<param name=\"resourceAllocationDenominator\">20</param>" +
                     "<param name=\"tBD_MinFrequency\">281000</param>" +
                     "<param name=\"tBD_MaxFrequency\">282000</param>" +
                     "<param name=\"tBD_MinPower\">21</param>" +
                     "<param name=\"tBD_MaxPower\">37</param>" +
                     "</obj>" + 
                     "<obj objID=\"dynamicResource:0\"><param name=\"objType\">TxSigPath_EUTRAFDD_RoE</param><param name=\"maxNumberOfResources\">8</param></obj>" +
                     "<obj objID=\"dynamicResource:1\"><param name=\"objType\">RxSigPath_EUTRAFDD_RoE</param><param name=\"maxNumberOfResources\">8</param></obj>" +
                     "<obj objID=\"antPort:0\"><param name=\"portLabel\">My Antenna port</param><param name=\"topology\">OMNI</param><param name=\"direction\">0</param><param name=\"angle\">0</param><param name=\"maxNumberOfTxSigPaths\">4</param><param name=\"maxNumberOfRxSigPaths\">4</param><param name=\"antennaElements\">1</param></obj>" +
                     "<obj objID=\"antPort:1\"><param name=\"portLabel\">My Antenna port</param><param name=\"topology\">OMNI</param><param name=\"direction\">0</param><param name=\"angle\">0</param><param name=\"maxNumberOfTxSigPaths\">4</param><param name=\"maxNumberOfRxSigPaths\">4</param><param name=\"antennaElements\">1</param></obj>" +
                     "<obj objID=\"ethLink:0\"><param name=\"portLabel\">ethLink:0</param><param name=\"portRoleCapability\">SO</param><param name=\"portRole\">SLAVE</param><param name=\"supportedLinkRate\">ffff</param><param name=\"requestedLinkRate\">1</param><param name=\"actualLinkRate\">1</param><param name=\"localAddress\">02:00:00:04:09:00</param></obj>" +
                     "<obj objID=\"dataLink:0\"><param name=\"portLabel\">link Label</param><param name=\"type\">ETH</param><param name=\"reference\">ethLink:0</param><param name=\"maxNumberOfTxSigPaths\">8</param><param name=\"maxNumberOfRxSigPaths\">8</param></obj>" +
                     "</getParamResp>" +
                     "</body>" +
                     "</msg>";
        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendModifyParamResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<modifyParamResp>" +
                     "<globResult>FAIL_PARAMETER_FAIL</globResult>" +
                     "<obj objID=\"TxSigPath_EUTRAFDD_RoE:0\"><param name=\"maxTxPwr\"/><result>FAIL_PARAM_LOCKREQUIRED</result></obj>" +
                     "</modifyParamResp>" +
                     "</body>" +
                     "</msg>";

        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void sendGetFaultResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<getFaultResp>" +
                     "<result>SUCCESS</result>" +
                     "<obj objID=\"RE:0\"><fault><faultID>FAULT_RE_OVERTEMP</faultID><severity>DEGRADED</severity><timestamp>2012-02-12T16:35:00Z</timestamp><descr>PA temp too high; Pout reduced</descr><affectedObj>TxSigPath_EUTRA:0</affectedObj><affectedObj>TxSigPath_EUTRA:1</affectedObj></fault><fault><faultID>FAULT_VSWR_OUTOF_RANGE</faultID><severity>WARNING</severity><timestamp>2012-02-12T16:01:05Z</timestamp></fault></obj>" +
                     "</getFaultResp>" +
                     "</body>" +
                     "</msg>";

        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendGetStateResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<getStateResp>" +
                     "<result>SUCCESS</result>" +
                     "<obj objID=\"RE:0\"><state type=\"FST\">DISABLED</state><state type=\"AST\">LOCKED</state></obj>" +
                     "<obj objID=\"dynamicResource:0\"><state type=\"FST\">DISABLED</state><state type=\"AST\">LOCKED</state></obj>" +
                     "<obj objID=\"dynamicResource:1\"><state type=\"FST\">DISABLED</state><state type=\"AST\">LOCKED</state></obj>" +
                     "<obj objID=\"antPort:0\"><state type=\"FST\">DISABLED</state><state type=\"AST\">LOCKED</state></obj>" +
                     "<obj objID=\"antPort:1\"><state type=\"FST\">DISABLED</state><state type=\"AST\">LOCKED</state></obj>" +
                     "<obj objID=\"ethLink:0\"><state type=\"FST\">OPERATIONAL</state><state type=\"AST\">LOCKED</state></obj>" +
                     "<obj objID=\"dataLink:0\"><state type=\"FST\">DISABLED</state><state type=\"AST\">LOCKED</state></obj>" +
                     "</getStateResp>" +
                     "</body>" +
                     "</msg>";

        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendModifyStateResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<modifyStateResp>" +
                     "<result>FAIL_PRECONDITION_NOTMET</result>" +
                     "<obj objID=\"TxSigPath_EUTRAFDD_RoE:0\"><state type=\"AST\">UNLOCKED</state></obj>" +
                     "</modifyStateResp>" +
                     "</body>" +
                     "</msg>";

        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCreateObjResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<createObjResp>" +
                     "<globResult>SUCCESS</globResult>" +
                     "<obj objID=\"TxSigPath_EUTRAFDD_RoE:0\"><param name=\"antPort\"/><result>SUCCESS</result><param name=\"dataLink\"/><result>SUCCESS</result></obj>" +
                     "</createObjResp>" +
                     "</body>" +
                     "</msg>";

        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDeleteObjResp() {
        String msg = "<msg xmlns=\"http://uri.etsi.org/ori/002-2/v4.1.1\">" +
                     "<header>" +
                     "<msgType>RESP</msgType>" +
                     "<msgUID>" + uid + "</msgUID>" +
                     "</header>" +
                     "<body>" +
                     "<deleteObjResp>" +
                     "<result>SUCCESS</result>" +
                     "</deleteObjResp>" +
                     "</body>" +
                     "</msg>";

        try {
            out.writeBytes(msg);
            System.out.println("\n\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println(
                "Usage: java org.opendaylight.ocpplugin.OcpAgent <controller's ip address> <port number> <vendor id> <serial number>");
            System.exit(1);
        }
        System.out.println("\n\nStarting OCP Agent..");
        OcpAgent agent = new OcpAgent(args[2], args[3]);
        agent.createConnection(args[0], Integer.parseInt(args[1]));
    }

}
