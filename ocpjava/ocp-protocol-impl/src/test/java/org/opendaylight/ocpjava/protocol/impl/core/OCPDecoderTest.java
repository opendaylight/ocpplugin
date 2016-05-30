/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyShort;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import io.netty.channel.ChannelHandlerContext;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;
import org.opendaylight.ocpjava.protocol.impl.core.XmlAttribute;
import org.opendaylight.ocpjava.protocol.impl.core.XmlDocumentStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlDocumentEnd;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.AfterClass;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializationFactory;
import org.opendaylight.yangtools.yang.binding.DataObject;

/**
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class OCPDecoderTest {

    @Mock ChannelHandlerContext mockChHndlrCtx ;
    @Mock DeserializationFactory mockDeserializationFactory ;
    @Mock DataObject mockDataObject ;

    OCPDecoder ocpDecoder ;
    private List<Object> writeObj;
    private DefaultMessageWrapper inMsg;
    private List<Object> outList;

    private static final XmlDocumentEnd XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;
    
    
    /* OCPDecoderTest Sample
    <?xml version="1.0" encoding="UTF-8"?>
    <msg xmlns="http://uri.etsi.org/ori/002-2/v4.1.1"> 
        <header>
            <msgType>IND</msgType>
            <msgUID>0</msgUID>
        </header>
        <body>
            <helloInd>
                <version>4.1.1</version>
                <vendorId>MTI</vendorId>
                <serialNumber>123</serialNumber>
            </helloInd>
        </body>
    </msg>
    */

    /**
     * Sets up test environment
     *
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ocpDecoder = new OCPDecoder() ;
        ocpDecoder.setDeserializationFactory( mockDeserializationFactory ) ;
        writeObj = new ArrayList<>();
        writeObj.add(new XmlDocumentStart("UTF-8", "1.0", false, ""));
        writeObj.add(XML_DOCUMENT_END);
        XmlElementStart msg = new XmlElementStart("msg", "", "");
        XmlAttribute xmlns = new XmlAttribute("", "xmlns", "", "http://uri.etsi.org/ori/002-2/v4.1.1", "");
        msg.attributes().add(xmlns);
        writeObj.add(msg);
        writeObj.add(new XmlElementStart("header", "", ""));
        writeObj.add(new XmlElementStart("msgType", "", ""));
            writeObj.add(new XmlCharacters("IND"));
            writeObj.add(new XmlElementEnd("msgType", "", ""));
            writeObj.add(new XmlElementStart("msgUID", "", ""));
                writeObj.add(new XmlCharacters("0"));
            writeObj.add(new XmlElementEnd("msgUID", "", ""));
        writeObj.add(new XmlElementEnd("header", "", ""));
        writeObj.add(new XmlElementStart("body", "", ""));
            writeObj.add(new XmlElementStart("helloInd", "", ""));
                writeObj.add(new XmlElementStart("version", "", ""));
                    writeObj.add(new XmlCharacters("4.1.1"));
                writeObj.add(new XmlElementEnd("version", "", ""));
                writeObj.add(new XmlElementStart("versionId", "", ""));
                    writeObj.add(new XmlCharacters("MTI"));
                writeObj.add(new XmlElementEnd("versionId", "", ""));
                writeObj.add(new XmlElementStart("serialNumber", "", ""));
                    writeObj.add(new XmlCharacters("123"));
                writeObj.add(new XmlElementEnd("serialNumber", "", ""));
            writeObj.add(new XmlElementEnd("helloInd", "", ""));
        writeObj.add(new XmlElementEnd("body", "", ""));
        writeObj.add(new XmlElementEnd("msg", "", ""));
        
        //example, healthCheck Req Type:0, version:1
        inMsg = new DefaultMessageWrapper( (short)1, 0, writeObj );
        outList = new ArrayList<>();
    }

    /**
     *
     */
    @Test
    public void testDecode() {
        when(mockDeserializationFactory.deserialize( anyShort(), anyInt(), any(List.class))).thenReturn(mockDataObject);
        try {
            ocpDecoder.decode(mockChHndlrCtx, inMsg, outList);
        } catch (Exception e) {
            Assert.fail();
        }

        // Verify that the message buf was released...
        assertEquals( mockDataObject, outList.get(0) ) ;
        assertEquals( 0, writeObj.size() ) ;
    }

    /**
     *
     */
    @Test
    public void testDecodeDeserializeException() {
        when(mockDeserializationFactory.deserialize( anyShort(), anyInt(), any(List.class)))
        .thenThrow(new IllegalArgumentException()) ;

        try {
            ocpDecoder.decode(mockChHndlrCtx, inMsg, outList);
        } catch (Exception e) {
            Assert.fail();
        }

        // Verify that the message buf was released...
        assertEquals( 0, outList.size() ) ;
        assertEquals( 0, writeObj.size() ) ;
    }

    /**
     *
     */
    @Test
    public void testDecodeDeserializeNull() {
        when(mockDeserializationFactory.deserialize( anyShort(), anyInt(), any(List.class)))
        .thenReturn(null) ;

        try {
            ocpDecoder.decode(mockChHndlrCtx, inMsg, outList);
        } catch (Exception e) {
            Assert.fail();
        }

        // Verify that the message buf was released...
        assertEquals( 0, outList.size() ) ;
        assertEquals( 0, writeObj.size() ) ;
    }
}
