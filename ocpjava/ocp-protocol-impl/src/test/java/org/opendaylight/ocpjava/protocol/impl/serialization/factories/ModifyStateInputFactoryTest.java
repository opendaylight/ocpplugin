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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateinput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateinput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateinput.obj.State;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifystateinput.obj.StateBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateVal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class ModifyStateInputFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyStateInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<ModifyStateInput> modifyStateInputFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        modifyStateInputFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, ModifyStateInput.class));
    }

    /**
     * Testing of {@link HealthCheckInputMessageFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        ModifyStateInputBuilder hib = new ModifyStateInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("MODIFYSTATEREQ"));
        boolean testEvent = true;
        String testObjId = "ALL";
        String testType = "AST";
        String testVal = "UNLOCKED";
        
        //set xid
        Method m2_t = hib.getClass().getMethod("setXid", Long.class);
        m2_t.invoke(hib, new Long(0));

        //set Obj
        ObjBuilder objbuilder = new ObjBuilder();
        
        StateBuilder statebuilder = new StateBuilder();
        List<State> slist = new ArrayList();
        statebuilder.setName(StateType.valueOf(testType));
        statebuilder.setValue(StateVal.valueOf(testVal));
        slist.add(statebuilder.build());
        objbuilder.setState(slist);
        
        objbuilder.setId(new ObjId(testObjId));
        List<Obj> objlist = new ArrayList();
        objlist.add(objbuilder.build());
        
        Method m4_t = hib.getClass().getMethod("setObj", List.class);
        m4_t.invoke(hib, objlist);
        
        ModifyStateInput hi = hib.build();
        LOGGER.debug("ModifyStateInputFactoryTest - hi objId value = {}", hi.getObj().get(0).getId().getValue());    

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        modifyStateInputFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOGGER.debug("ModifyStateInputFactoryTest - out = {}", out.readableBytes());    
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
