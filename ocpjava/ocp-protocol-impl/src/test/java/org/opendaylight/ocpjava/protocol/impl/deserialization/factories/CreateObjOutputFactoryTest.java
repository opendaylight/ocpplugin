/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization.factories;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.opendaylight.ocpjava.protocol.api.extensibility.DeserializerRegistry;
import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;
import org.opendaylight.ocpjava.protocol.api.keys.MessageCodeKey;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializerRegistryImpl;
import org.opendaylight.ocpjava.protocol.impl.util.BufferHelper;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;
import org.opendaylight.ocpjava.protocol.impl.core.XmlAttribute;
import org.opendaylight.ocpjava.protocol.impl.core.XmlDocumentStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlDocumentEnd;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class CreateObjOutputFactoryTest {

    private OCPDeserializer<CreateObjOutput> createObjOutputFactory;
    private static final XmlDocumentEnd XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;
    /**
     * Initializes deserializer registry and lookups correct deserializer
     */
    @Before
    public void startUp() {
        DeserializerRegistry registry = new DeserializerRegistryImpl();
        registry.init();
        createObjOutputFactory = registry.getDeserializer(
                new MessageCodeKey(EncodeConstants.OCP_VERSION_ID, 11, CreateObjOutput.class));
    }

    /**
     * Testing {@link SetTimeOutputFactory} for correct translation into POJO
     */
    @Test
    public void testWithoutElements() {
        List<Object> bb = new ArrayList<>(); 
        CreateObjOutput builtByFactory = BufferHelper.deserialize(createObjOutputFactory, bb);
        
        Assert.assertNull("Wrong elements", builtByFactory.getMsgType());
        Assert.assertNull("Wrong elements", builtByFactory.getGlobResult());
        Assert.assertNull("Wrong elements", builtByFactory.getXid());
    }
    
    /**
     * Testing {@link SetTimeOutputFactory} for correct translation into POJO
     */
    @Test
    public void testWithElements() {
        List<Object> bb = new ArrayList<>(); 
        bb.add(new XmlDocumentStart("", "1.0", false, ""));
        bb.add(XML_DOCUMENT_END);
        XmlElementStart msg = new XmlElementStart("msg", "", "");
        XmlAttribute xmlns = new XmlAttribute("", "xmlns", "", "http://uri.etsi.org/ori/002-2/v4.1.1", "");
        msg.attributes().add(xmlns);
        bb.add(msg);
            bb.add(new XmlElementStart("header", "", ""));
                bb.add(new XmlElementStart("msgType", "", ""));
                    bb.add(new XmlCharacters("RESP"));
                bb.add(new XmlElementEnd("msgType", "", ""));
                bb.add(new XmlElementStart("msgUID", "", ""));
                    bb.add(new XmlCharacters("0"));
                bb.add(new XmlElementEnd("msgUID", "", ""));
            bb.add(new XmlElementEnd("header", "", ""));
            bb.add(new XmlElementStart("body", "", ""));

                bb.add(new XmlElementStart("createObjResp", "", ""));

                    bb.add(new XmlElementStart("globResult", "", ""));
                    bb.add(new XmlCharacters("FAIL_PARAMETER_FAIL"));
                    bb.add(new XmlElementEnd("globResult", "", ""));

                    XmlElementStart objTag = new XmlElementStart("obj", "", "");
                    XmlAttribute objId = new XmlAttribute("", "objID", "", "", "exampleObj:1");
                    objTag.attributes().add(objId);
                    bb.add(objTag);
        
                        XmlElementStart param1Tag = new XmlElementStart("param", "", "");
                        XmlAttribute name1 = new XmlAttribute("", "name", "", "", "parameter1");
                        param1Tag.attributes().add(name1);
                        bb.add(param1Tag);
                        bb.add(new XmlElementEnd("param", "", ""));
                
                        bb.add(new XmlElementStart("result", "", ""));
                        bb.add(new XmlCharacters("SUCCESS"));
                        bb.add(new XmlElementEnd("result", "", ""));

                        XmlElementStart param2Tag = new XmlElementStart("param", "", "");
                        XmlAttribute name2 = new XmlAttribute("", "name", "", "", "parameter2");
                        param2Tag.attributes().add(name2);
                        bb.add(param2Tag);
                        bb.add(new XmlElementEnd("param", "", ""));
            
                        bb.add(new XmlElementStart("result", "", ""));
                        bb.add(new XmlCharacters("SUCCESS"));
                        bb.add(new XmlElementEnd("result", "", ""));
                
                        XmlElementStart param3Tag = new XmlElementStart("param", "", "");
                        XmlAttribute name3 = new XmlAttribute("", "name", "", "", "parameter3");
                        param3Tag.attributes().add(name3);
                        bb.add(param3Tag);
                        bb.add(new XmlElementEnd("param", "", ""));
                
                        bb.add(new XmlElementStart("result", "", ""));
                        bb.add(new XmlCharacters("FAIL_VALUE_TYPE_ERROR"));
                        bb.add(new XmlElementEnd("result", "", ""));
            
                    bb.add(new XmlElementEnd("obj", "", ""));
                bb.add(new XmlElementEnd("createObjResp", "", ""));
            
            bb.add(new XmlElementEnd("body", "", ""));
        bb.add(new XmlElementEnd("msg", "", ""));

        CreateObjOutput builtByFactory = BufferHelper.deserialize(createObjOutputFactory, bb);

        BufferHelper.checkHeaderV10(builtByFactory);
        Assert.assertNotNull("Wrong elements", builtByFactory.getMsgType());
        Assert.assertNotNull("Wrong elements", builtByFactory.getGlobResult());
        Assert.assertNotNull("Wrong elements", builtByFactory.getXid());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getId().getValue());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getParam().get(0).getName());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getParam().get(0).getResult());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getParam().get(1).getName());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getParam().get(1).getResult());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getParam().get(2).getName());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getParam().get(2).getResult());
    }
}
