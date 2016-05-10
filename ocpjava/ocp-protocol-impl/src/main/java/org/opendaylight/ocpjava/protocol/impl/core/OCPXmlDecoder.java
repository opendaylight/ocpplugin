/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionFacade;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import com.fasterxml.aalto.AsyncInputFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

/**
 * Transforms OCP XML Parser
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */

public class OCPXmlDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCPXmlDecoder.class);
    private ConnectionFacade connectionFacade;
    private boolean firstTlsPass = false;
    private int unknownMsg = 99;
    private int msgLength = 6;

    private static final AsyncXMLInputFactory XML_INPUT_FACTORY = new InputFactoryImpl();
    private AsyncXMLStreamReader streamReader = XML_INPUT_FACTORY.createAsyncXMLStreamReader();
    private AsyncInputFeeder streamFeeder = streamReader.getInputFeeder();

    private List<Object> xmlElms;
    private boolean bodyElmFound;
    private int msgType;

    private int charCnt = 0;
    private String charCombine = "";
    
    public OCPXmlDecoder(ConnectionFacade connectionFacade, boolean tlsPresent) {
        LOGGER.trace("Creating OCPXmlDecoder");
        if (tlsPresent) {
            firstTlsPass = true;
        }
        this.connectionFacade = connectionFacade;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (firstTlsPass) {
            connectionFacade.fireConnectionReadyNotification();
            firstTlsPass = false;
        }
        
        byte[] buffer = new byte[in.readableBytes()];
        in.readBytes(buffer);
        
        //transfer from buffer to String
        String valueS = new String(buffer, "UTF-8");
        LOGGER.trace("decode valueS: {}", valueS);
        
        try {
            streamFeeder.feedInput(buffer, 0, buffer.length);
        } catch (XMLStreamException exception) {
            in.skipBytes(in.readableBytes());
            throw exception;
        }

        while (!streamFeeder.needMoreInput()) {
            int type = streamReader.next();
            switch (type) {
                case XMLStreamConstants.START_DOCUMENT:
                    XmlDocumentStart xmlDocumentStart = new XmlDocumentStart(streamReader.getEncoding(), streamReader.getVersion(), streamReader.isStandalone(), streamReader.getCharacterEncodingScheme());
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    XmlElementStart elementStart = new XmlElementStart(streamReader.getLocalName(),
                            streamReader.getName().getNamespaceURI(), streamReader.getPrefix());
                    for (int x = 0; x < streamReader.getAttributeCount(); x++) {
                        XmlAttribute attribute = new XmlAttribute(streamReader.getAttributeType(x),
                                streamReader.getAttributeLocalName(x), streamReader.getAttributePrefix(x),
                                streamReader.getAttributeNamespace(x), streamReader.getAttributeValue(x));
                        elementStart.attributes().add(attribute);
                    }
                    for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
                        XmlNamespace namespace = new XmlNamespace(streamReader.getNamespacePrefix(x),
                                streamReader.getNamespaceURI(x));
                        elementStart.namespaces().add(namespace);
                    }
                    if (elementStart.name().equals("msg")) {
                        xmlElms = new ArrayList<>();                   
                        bodyElmFound = false;
                    }
                    else if (elementStart.name().equals("body")) {
                        bodyElmFound = true;
                    }  
                    else if (bodyElmFound) {
	                boolean isOcpMsgType = EnumSet.allOf(OcpMsgType.class).toString().contains(elementStart.name().toUpperCase());
	                if(!isOcpMsgType){
	                    LOGGER.warn("OCPXmlDecoder - unknown OcpMsgType format");
                            //unknown Message
                            msgType = unknownMsg;
	                }
                        else{
                            msgType = OcpMsgType.valueOf(elementStart.name().toUpperCase()).getIntValue();
                        }
                        bodyElmFound = false;
                        LOGGER.trace("Message start: " + elementStart.name());
                    }
                    xmlElms.add(elementStart);
                    break;
                case XMLStreamConstants.END_ELEMENT:

                    if(charCnt >= 1){
                        LOGGER.info("charCnt: {}, charCombine: {}", charCnt, charCombine.replace("\n", "").replace(" ", ""));
                        XmlCharacters elementChars = new XmlCharacters(charCombine.replace("\n", "").replace(" ", ""));
                        xmlElms.add(elementChars);
                    }
                    charCnt = 0;
                    charCombine = "";
                    
                    XmlElementEnd elementEnd = new XmlElementEnd(streamReader.getLocalName(),
                            streamReader.getName().getNamespaceURI(), streamReader.getPrefix());
                    for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
                        XmlNamespace namespace = new XmlNamespace(streamReader.getNamespacePrefix(x),
                                streamReader.getNamespaceURI(x));
                        elementEnd.namespaces().add(namespace);
                    }
                    xmlElms.add(elementEnd);
                    if (elementEnd.name().equals("msg")) {
                        LOGGER.trace("Message end: " + elementEnd.name());
                        out.add(new DefaultMessageWrapper((short)1, msgType, xmlElms));
                        streamFeeder.endOfInput(); 
                        streamReader = XML_INPUT_FACTORY.createAsyncXMLStreamReader();
                        streamFeeder = streamReader.getInputFeeder();

                        LOGGER.trace("valueS length: " + valueS.length());
                        LOGGER.trace("valueS indexOf: " + valueS.indexOf("</msg>"));

                        // </msg> length is 6
                        int endidx = valueS.indexOf("</msg>") + msgLength;
                        LOGGER.trace("idx: " + endidx);
                        
                        //handle remain XML messages
                        if(valueS.length() - endidx > 0) {
                            valueS = valueS.substring(endidx);
                            LOGGER.trace("valueS.length() : " + valueS.length());
                            byte[] bytesData = valueS.getBytes();
                            streamFeeder.feedInput(bytesData, 0, bytesData.length);
                        }
                    }
                    break;
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    xmlElms.add(new XmlProcessingInstruction(streamReader.getPIData(), streamReader.getPITarget()));
                    break;
                case XMLStreamConstants.CHARACTERS:
                    charCnt += 1;
                    charCombine = charCombine.concat(String.valueOf(streamReader.getText()));
                    break;
                case XMLStreamConstants.COMMENT:
                    xmlElms.add(new XmlComment(streamReader.getText()));
                    break;
                case XMLStreamConstants.SPACE:
                    xmlElms.add(new XmlSpace(streamReader.getText()));
                    break;
                case XMLStreamConstants.ENTITY_REFERENCE:
                    xmlElms.add(new XmlEntityReference(streamReader.getLocalName(), streamReader.getText()));
                    break;
                case XMLStreamConstants.DTD:
                    xmlElms.add(new XmlDTD(streamReader.getText()));
                    break;
                case XMLStreamConstants.CDATA:
                    xmlElms.add(new XmlCdata(streamReader.getText()));
                    break;
                default:
                    break;
            }
        }
    }

}
