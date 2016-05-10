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
    private static final String MSG_GET_PARAM_REQ = "getParamReq";

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
                else if (reader.getLocalName().equals(MSG_GET_PARAM_REQ)) {
                    sendGetParamResp();
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
                     "<helloInd>\n" +
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
                     "</obj>" + 
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
