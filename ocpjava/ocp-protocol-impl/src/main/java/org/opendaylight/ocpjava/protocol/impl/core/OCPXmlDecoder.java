package org.opendaylight.ocpjava.protocol.impl.core;

import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionFacade;
import org.opendaylight.ocpjava.util.ByteBufUtils;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


public class OCPXmlDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCPXmlDecoder.class);
    private static final XMLInputFactory factory = XMLInputFactory.newFactory();

    private List<Object> out;    
    private String buf = "";
    private List<Object> xmlElms;
    private boolean bodyElmFound;
    private int msgType;


    public OCPXmlDecoder(ConnectionFacade connectionFacade, boolean tlsPresent) {
        LOGGER.trace("Creating OCPXmlDecoder");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() == 0)
            return;

        this.out = out;

        byte[] bs = new byte[in.readableBytes()];
        in.readBytes(bs);
        buf += new String(bs, "UTF-8");

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

    private void parseDocument(String msg) {
        try {
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

    private void parseEvent(XMLStreamReader streamReader) {

        switch (streamReader.getEventType()) {
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
	            if (!isOcpMsgType) {
	                LOGGER.warn("OCPXmlDecoder - unknown OcpMsgType format");
	                msgType = 99; //unknown Message
	            }
	            else {
                        msgType = OcpMsgType.valueOf(elementStart.name().toUpperCase()).getIntValue();
                    }
                    bodyElmFound = false;
                    LOGGER.trace("Message start: " + elementStart.name());
                }
                xmlElms.add(elementStart);
                break;

            case XMLStreamConstants.END_ELEMENT:
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
                }
                break;

            case XMLStreamConstants.CHARACTERS:
                XmlCharacters elementChars = new XmlCharacters(streamReader.getText());
                xmlElms.add(elementChars);
                break;

            default:
        }

    }

}
