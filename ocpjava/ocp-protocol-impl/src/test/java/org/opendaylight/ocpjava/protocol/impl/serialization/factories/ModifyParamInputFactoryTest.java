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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifyparaminput.Param;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifyparaminput.ParamBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class ModifyParamInputFactoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(ModifyParamInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<ModifyParamInput> modifyParamInputFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        modifyParamInputFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, ModifyParamInput.class));
    }

    /**
     * Testing of {@link ModifyParamInputFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        ModifyParamInputBuilder hib = new ModifyParamInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("MODIFYPARAMREQ"));
        boolean testEvent = true;
        String testObjId = "ALL";
        //set xid
        Method m_t = hib.getClass().getMethod("setXid", Long.class);
        m_t.invoke(hib, new Long(0));

        //set ObjId
        Method m2_t = hib.getClass().getMethod("setObjId", ObjId.class);
        m2_t.invoke(hib, new ObjId(testObjId));
        
        //set Params
        List<Param> plist = new ArrayList();
        ParamBuilder parambuilder1 = new ParamBuilder();
        parambuilder1.setName("parameter1");
        parambuilder1.setValue("value1");
        plist.add(parambuilder1.build());
        ParamBuilder parambuilder2 = new ParamBuilder();
        parambuilder2.setName("parameter2");
        parambuilder2.setValue("value2");
        plist.add(parambuilder2.build());
        ParamBuilder parambuilder3 = new ParamBuilder();
        parambuilder3.setName("parameter3");
        parambuilder3.setValue("a wrong value");
        plist.add(parambuilder3.build());

        Method m3_t = hib.getClass().getMethod("setParam", List.class);
        m3_t.invoke(hib, plist);
        
        ModifyParamInput hi = hib.build();
        LOG.debug("ModifyParamInputFactoryTest - hi objId value = {}", hi.getObjId());

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        modifyParamInputFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOG.debug("ModifyParamInputFactoryTest - out = {}", out.readableBytes());    
        byte[] bytes = new byte[out.readableBytes()];
        int readerIndex = out.readerIndex();
        out.getBytes(readerIndex, bytes);

        String buf = new String(bytes, "UTF-8");
        StringBuilder seq = new StringBuilder("");
        seq.append("<obj objID=\"");
        seq.append(testObjId);
        seq.append("\">");
        boolean checkVal = buf.contains(seq);
        
        //Check and compare elements
        Assert.assertEquals("Wrong length", true, checkVal);
    }
}
