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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateAllType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class GetStateInputFactoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(GetStateInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<GetStateInput> getStateInputFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        getStateInputFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, GetStateInput.class));
    }

    /**
     * Testing of {@link HealthCheckInputMessageFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        GetStateInputBuilder hib = new GetStateInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("GETSTATEREQ"));
        boolean testEvent = true;
        String testObjId = "exampleObj:0";
        String testStateType = "ALL";
        
        //set xid
        Method m2_t = hib.getClass().getMethod("setXid", Long.class);
        m2_t.invoke(hib, new Long(0));

        //set eventDrivenReporting
        Method m3_t = hib.getClass().getMethod("setEventDrivenReporting", Boolean.class);
        m3_t.invoke(hib, new Boolean(testEvent));

        //set ObjId
        Method m4_t = hib.getClass().getMethod("setObjId", ObjId.class);
        m4_t.invoke(hib, new ObjId(testObjId));
        
        //set State Type
        Method m5_t = hib.getClass().getMethod("setStateType", StateAllType.class);
        m5_t.invoke(hib, StateAllType.valueOf(testStateType));

        GetStateInput hi = hib.build();
        LOG.debug("GetStateInputFactoryTest - hi objId value = {}", hi.getObjId());

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        getStateInputFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOG.debug("GetStateInputFactoryTest - out = {}", out.readableBytes());    
        byte[] bytes = new byte[out.readableBytes()];
        int readerIndex = out.readerIndex();
        out.getBytes(readerIndex, bytes);

        String buf = new String(bytes, "UTF-8");
        StringBuilder seq = new StringBuilder("");
        seq.append("<obj objID=\"");
        seq.append(testObjId);
        seq.append("\">");
        boolean checkVal = buf.contains(seq);
        
        StringBuilder seq2 = new StringBuilder("");
        seq2.append("<eventDrivenReporting>");
        seq2.append(String.valueOf(testEvent));
        seq2.append("</eventDrivenReporting>");
        boolean checkVal2 = buf.contains(seq2);
        
        //Check and compare elements
        Assert.assertEquals("Wrong length", true, checkVal);
        Assert.assertEquals("Wrong length", true, checkVal2);
    }
}
