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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getfaultinput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getfaultinput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class GetFaultInputFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetFaultInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<GetFaultInput> getFaultInputFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        getFaultInputFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, GetFaultInput.class));
    }

    /**
     * Testing of {@link HealthCheckInputMessageFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        GetFaultInputBuilder hib = new GetFaultInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("GETFAULTREQ"));
        boolean testEvent = true;
        String testObjId = "ALL";
        //set xid
        Method m2_t = hib.getClass().getMethod("setXid", Long.class);
        m2_t.invoke(hib, new Long(0));

        //set eventDrivenReporting
        Method m3_t = hib.getClass().getMethod("setEventDrivenReporting", Boolean.class);
        m3_t.invoke(hib, new Boolean(testEvent));

        //set Obj
        ObjBuilder objbuilder = new ObjBuilder();
        objbuilder.setId(new ObjId(testObjId));
        List<Obj> objlist = new ArrayList();
        objlist.add(objbuilder.build());
        
        Method m4_t = hib.getClass().getMethod("setObj", List.class);
        m4_t.invoke(hib, objlist);
        
        GetFaultInput hi = hib.build();
        LOGGER.debug("GetFaultInputFactoryTest - hi objId value = {}", hi.getObj().get(0).getId().getValue());    

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        getFaultInputFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOGGER.debug("HealthCheckInputMessageFactoryTest - out = {}", out.readableBytes());    
        byte[] bytes = new byte[out.readableBytes()];
        int readerIndex = out.readerIndex();
        out.getBytes(readerIndex, bytes);

        String buf = new String(bytes, "UTF-8");
        StringBuilder seq = new StringBuilder("");
        seq.append("<eventDrivenReporting>");
        seq.append(String.valueOf(testEvent));
        seq.append("</eventDrivenReporting>");
        boolean checkVal = buf.contains(seq);

        StringBuilder seq2 = new StringBuilder("");
        seq2.append("<obj objID=\"");
        seq2.append(testObjId);
        seq2.append("\"/>");
        boolean checkVal2 = buf.contains(seq2);
        
        //Check and compare elements
        Assert.assertEquals("Wrong length", true, checkVal);
        Assert.assertEquals("Wrong length", true, checkVal2);
    }
}
