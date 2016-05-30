/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;

import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.protocol.impl.core.XmlAttribute;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;
import org.opendaylight.ocpjava.protocol.impl.core.XmlDocumentEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlDocumentStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class DeserializationFactoryTest {
    private static final XmlDocumentEnd XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;
    
    /**
     * Test deserializer lookup & deserialization
     */
    @Test
    public void test() {
        DeserializerRegistryImpl registry = new DeserializerRegistryImpl();
        registry.init();
        DeserializationFactory factory = new DeserializationFactory();
        factory.setRegistry(registry);

        List<Object> bb = new ArrayList<>(); 
        bb.add(new XmlDocumentStart("UTF-8", "1.0", false, ""));
        bb.add(XML_DOCUMENT_END);
        XmlElementStart msg = new XmlElementStart("msg", "", "");
        XmlAttribute xmlns = new XmlAttribute("", "xmlns", "", "http://uri.etsi.org/ori/002-2/v4.1.1", "");
        msg.attributes().add(xmlns);        
        bb.add(msg);
        bb.add(new XmlElementStart("header", "", ""));
            bb.add(new XmlElementStart("msgType", "", ""));
                bb.add(new XmlCharacters("IND"));
            bb.add(new XmlElementEnd("msgType", "", ""));
            bb.add(new XmlElementStart("msgUID", "", ""));
                bb.add(new XmlCharacters("0"));
            bb.add(new XmlElementEnd("msgUID", "", ""));
        bb.add(new XmlElementEnd("header", "", ""));
        bb.add(new XmlElementStart("body", "", ""));

            bb.add(new XmlElementStart("helloInd", "", ""));
                bb.add(new XmlElementStart("version", "", ""));
                    bb.add(new XmlCharacters("4.1.1"));
                bb.add(new XmlElementEnd("version", "", ""));
                bb.add(new XmlElementStart("versionId", "", ""));
                    bb.add(new XmlCharacters("MTI"));
                bb.add(new XmlElementEnd("versionId", "", ""));
                bb.add(new XmlElementStart("serialNumber", "", ""));
                    bb.add(new XmlCharacters("123"));
                bb.add(new XmlElementEnd("serialNumber", "", ""));
            bb.add(new XmlElementEnd("helloInd", "", ""));
        
        bb.add(new XmlElementEnd("body", "", ""));
    bb.add(new XmlElementEnd("msg", "", ""));
        
        factory.deserialize(EncodeConstants.OCP_VERSION_ID, 1, bb);
        assertEquals("Deserialization failed", 25, bb.size());
    }

    
    /**
     * Test deserializer lookup & deserialization
     */
    @Test(expected=NullPointerException.class)
    public void testNullPointerDeserializer() {
        DeserializerRegistryImpl registry = new DeserializerRegistryImpl();
        registry.init();
        DeserializationFactory factory = new DeserializationFactory();
        factory.setRegistry(registry);
        List<Object> bb = null;
        factory.deserialize((short)1, 1, bb);
    }
    
    
    /**
     * Test deserializer lookup & deserialization
     */
    @Test(expected=IllegalStateException.class)    
    public void testNotExistingDeserializer() {
        DeserializerRegistryImpl registry = new DeserializerRegistryImpl();
        registry.init();
        DeserializationFactory factory = new DeserializationFactory();
        factory.setRegistry(registry);
        
        List<Object> bb = new ArrayList<>(); 
        bb.add(new XmlDocumentStart("UTF-8", "1.0", false, ""));
        bb.add(XML_DOCUMENT_END);
        XmlElementStart msg = new XmlElementStart("msg", "", "");
        XmlAttribute xmlns = new XmlAttribute("", "xmlns", "", "http://uri.etsi.org/ori/002-2/v4.1.1", "");
        msg.attributes().add(xmlns);        
        bb.add(msg);
        bb.add(new XmlElementStart("header", "", ""));
            bb.add(new XmlElementStart("msgType", "", ""));
                bb.add(new XmlCharacters("IND"));
            bb.add(new XmlElementEnd("msgType", "", ""));
            bb.add(new XmlElementStart("msgUID", "", ""));
                bb.add(new XmlCharacters("0"));
            bb.add(new XmlElementEnd("msgUID", "", ""));
        bb.add(new XmlElementEnd("header", "", ""));
        bb.add(new XmlElementStart("body", "", ""));

            bb.add(new XmlElementStart("helloInd", "", ""));
                bb.add(new XmlElementStart("version", "", ""));
                    bb.add(new XmlCharacters("4.1.1"));
                bb.add(new XmlElementEnd("version", "", ""));
                bb.add(new XmlElementStart("versionId", "", ""));
                    bb.add(new XmlCharacters("MTI"));
                bb.add(new XmlElementEnd("versionId", "", ""));
                bb.add(new XmlElementStart("serialNumber", "", ""));
                    bb.add(new XmlCharacters("123"));
                bb.add(new XmlElementEnd("serialNumber", "", ""));
            bb.add(new XmlElementEnd("helloInd", "", ""));
        
        bb.add(new XmlElementEnd("body", "", ""));
        bb.add(new XmlElementEnd("msg", "", ""));
        
        factory.deserialize((short)0, 1, bb);
    }
    
}
