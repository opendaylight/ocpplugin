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

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class GetFaultOutputFactoryTest {

    private OCPDeserializer<GetFaultOutput> getFaultOutputFactory;
    private static final XmlDocumentEnd XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;
    /**
     * Initializes deserializer registry and lookups correct deserializer
     */
    @Before
    public void startUp() {
        DeserializerRegistry registry = new DeserializerRegistryImpl();
        registry.init();
        getFaultOutputFactory = registry.getDeserializer(
                new MessageCodeKey(EncodeConstants.OCP_VERSION_ID, 20, GetFaultOutput.class));
    }

    /**
     * Testing {@link GetFaultOutputFactory} for correct translation into POJO
     */
    @Test
    public void testWithoutElements() {
        List<Object> bb = new ArrayList<>(); 
        GetFaultOutput builtByFactory = BufferHelper.deserialize(getFaultOutputFactory, bb);
        
        Assert.assertNull("Wrong elements", builtByFactory.getMsgType());
        Assert.assertNull("Wrong elements", builtByFactory.getResult());
        Assert.assertNull("Wrong elements", builtByFactory.getXid());
    }
    
    /**
     * Testing {@link GetFaultOutputFactory} for correct translation into POJO
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
                bb.add(new XmlElementStart("getFaultResp", "", ""));
                
                    bb.add(new XmlElementStart("result", "", ""));
                    bb.add(new XmlCharacters("SUCCESS"));
                    bb.add(new XmlElementEnd("result", "", ""));
                
                    XmlElementStart objTag = new XmlElementStart("obj", "", "");
                    XmlAttribute objId = new XmlAttribute("", "objID", "", "", "RE:0");
                    objTag.attributes().add(objId);
                    bb.add(objTag);
                    
                    bb.add(new XmlElementStart("fault", "", ""));
                        bb.add(new XmlElementStart("faultID", "", ""));
                            bb.add(new XmlCharacters("FAULT_RE_OVERTEMP"));
                        bb.add(new XmlElementEnd("faultID", "", ""));
                        bb.add(new XmlElementStart("severity", "", ""));
                            bb.add(new XmlCharacters("DEGRADED"));
                        bb.add(new XmlElementEnd("severity", "", ""));
                        bb.add(new XmlElementStart("timestamp", "", ""));
                            bb.add(new XmlCharacters("2012-02-12T16:35:00Z"));
                        bb.add(new XmlElementEnd("timestamp", "", ""));                        
                        bb.add(new XmlElementStart("descr", "", ""));
                            bb.add(new XmlCharacters("PA temp too high; Pout reduced"));
                        bb.add(new XmlElementEnd("descr", "", ""));               
                        bb.add(new XmlElementStart("affectedObj", "", ""));
                            bb.add(new XmlCharacters("TxSigPath_EUTRA:0"));
                        bb.add(new XmlElementEnd("affectedObj", "", ""));
                        bb.add(new XmlElementStart("affectedObj", "", ""));
                            bb.add(new XmlCharacters("TxSigPath_EUTRA:1"));
                        bb.add(new XmlElementEnd("affectedObj", "", ""));
                    bb.add(new XmlElementEnd("fault", "", ""));

                    bb.add(new XmlElementStart("fault", "", ""));
                        bb.add(new XmlElementStart("faultID", "", ""));
                            bb.add(new XmlCharacters("FAULT_VSWR_OUTOF_RANGE"));
                        bb.add(new XmlElementEnd("faultID", "", ""));
                        bb.add(new XmlElementStart("severity", "", ""));
                            bb.add(new XmlCharacters("WARNING"));
                        bb.add(new XmlElementEnd("severity", "", ""));
                        bb.add(new XmlElementStart("timestamp", "", ""));
                            bb.add(new XmlCharacters("2012-02-12T16:01:05Z"));
                        bb.add(new XmlElementEnd("timestamp", "", "")); 
                    bb.add(new XmlElementEnd("fault", "", ""));
                    
                    bb.add(new XmlElementEnd("obj", "", ""));
                    
                bb.add(new XmlElementEnd("getFaultResp", "", ""));
            bb.add(new XmlElementEnd("body", "", ""));
        bb.add(new XmlElementEnd("msg", "", ""));

        GetFaultOutput builtByFactory = BufferHelper.deserialize(getFaultOutputFactory, bb);

        BufferHelper.checkHeaderV10(builtByFactory);
        Assert.assertNotNull("Wrong elements", builtByFactory.getMsgType());
        Assert.assertNotNull("Wrong elements", builtByFactory.getResult());
        Assert.assertNotNull("Wrong elements", builtByFactory.getXid());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getId().getValue());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(0).getId());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(0).getSeverity());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(0).getTimestamp());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(0).getDescr());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(0).getAffectedObj().get(0));
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(0).getAffectedObj().get(1));
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(1).getId());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(1).getSeverity());
        Assert.assertNotNull("Wrong elements", builtByFactory.getObj().get(0).getFault().get(1).getTimestamp());
    }
}
