/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.serialization.factories;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPSerializer;
import org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistry;
import org.opendaylight.ocpjava.protocol.api.keys.MessageTypeKey;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializerRegistryImpl;
import org.opendaylight.ocpjava.protocol.impl.util.BufferHelper;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.createobjinput.Param;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.createobjinput.ParamBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class CreateObjInputFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateObjInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<CreateObjInput> createObjInputFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        createObjInputFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, CreateObjInput.class));
    }

    /**
     * Testing of {@link CreateObjInputFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        CreateObjInputBuilder hib = new CreateObjInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("CREATEOBJREQ"));
        boolean testEvent = true;
        String testObjTypeId = "exampleObj";
        
        //set xid
        Method m2_t = hib.getClass().getMethod("setXid", Long.class);
        m2_t.invoke(hib, new Long(0));

        //set ObjType
        Method m3_t = hib.getClass().getMethod("setObjType", ObjType.class);
        m3_t.invoke(hib, new ObjType(testObjTypeId));

        //set params
        ParamBuilder parambuilder1 = new ParamBuilder();
        List<Param> plist = new ArrayList();
        parambuilder1.setName("parameter1");
        parambuilder1.setValue("value1");
        plist.add(parambuilder1.build());
        ParamBuilder parambuilder2 = new ParamBuilder();
        parambuilder2.setName("parameter2");
        parambuilder2.setValue("value2");
        plist.add(parambuilder2.build());
        Method m4_t = hib.getClass().getMethod("setParam", List.class);
        m4_t.invoke(hib, plist);
        
        CreateObjInput hi = hib.build();
        LOGGER.debug("CreateObjInputFactoryTest - hi objId value = {}", hi.getObjType());

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        createObjInputFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOGGER.debug("HealthCheckInputMessageFactoryTest - out = {}", out.readableBytes());    
        byte[] bytes = new byte[out.readableBytes()];
        int readerIndex = out.readerIndex();
        out.getBytes(readerIndex, bytes);

        String buf = new String(bytes, "UTF-8");
        StringBuilder seq = new StringBuilder("");
        seq.append("<param name=\"parameter1\">value1</param>");
        boolean checkVal = buf.contains(seq);

        StringBuilder seq2 = new StringBuilder("");
        seq2.append("<objType objTypeID=\"");
        seq2.append(testObjTypeId);
        seq2.append("\">");
        boolean checkVal2 = buf.contains(seq2);
        
        //Check and compare elements
        Assert.assertEquals("Wrong length", true, checkVal);
        Assert.assertEquals("Wrong length", true, checkVal2);
    }
}
