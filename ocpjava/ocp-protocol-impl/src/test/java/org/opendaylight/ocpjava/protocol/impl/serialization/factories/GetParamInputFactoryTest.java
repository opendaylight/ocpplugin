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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.Param;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.ParamBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class GetParamInputFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetParamInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<GetParamInput> getParamInputFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        getParamInputFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, GetParamInput.class));
    }

    /**
     * Testing of {@link GetParamInputFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        GetParamInputBuilder hib = new GetParamInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("GETPARAMREQ"));
        String testObjId = "RE:0";
        //set xid
        Method m2_t = hib.getClass().getMethod("setXid", Long.class);
        m2_t.invoke(hib, new Long(0));


        //set Obj
        ObjBuilder objbuilder = new ObjBuilder();
        ParamBuilder parambuilder1 = new ParamBuilder();
        List<Param> plist = new ArrayList();
        parambuilder1.setName("vendorID");
        plist.add(parambuilder1.build());
        objbuilder.setParam(plist);
        
        objbuilder.setId(new ObjId(testObjId));
        List<Obj> objlist = new ArrayList();
        objlist.add(objbuilder.build());
        
        Method m_t = hib.getClass().getMethod("setObj", List.class);
        m_t.invoke(hib, objlist);
        
        GetParamInput hi = hib.build();
        LOGGER.debug("GetParamInputFactoryTest - hi objId = {}", hi.getObj().get(0).getId().getValue());    
        LOGGER.debug("GetParamInputFactoryTest - hi param = {}", hi.getObj().get(0).getParam().get(0).getName());    

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        getParamInputFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOGGER.debug("GetParamInputFactoryTest - out = {}", out.readableBytes());    
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
