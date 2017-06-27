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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPSerializer;
import org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistry;
import org.opendaylight.ocpjava.protocol.api.keys.MessageTypeKey;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializerRegistryImpl;
import org.opendaylight.ocpjava.protocol.impl.util.BufferHelper;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
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
public class ReResetInputFactoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(ReResetInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<ReResetInput> reResetFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        reResetFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, ReResetInput.class));
    }

    /**
     * Testing of {@link ReResetInputFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        ReResetInputBuilder hib = new ReResetInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("RESETREQ"));
        String msgTag = "resetReq";
        
        //set xid
        Method m_t = hib.getClass().getMethod("setXid", Long.class);
        m_t.invoke(hib, new Long(0));

        ReResetInput hi = hib.build();
        LOG.debug("ReResetInputFactoryTest - hi Xid value = {}", hi.getXid());    

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        reResetFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOG.debug("ReResetInputFactoryTest - out = {}", out.readableBytes());    
        byte[] bytes = new byte[out.readableBytes()];
        int readerIndex = out.readerIndex();
        out.getBytes(readerIndex, bytes);

        StringBuilder seq = new StringBuilder("");
        seq.append("resetReq");
        String buf = new String(bytes, "UTF-8");
        boolean checkVal = buf.contains(seq);

        //Check and compare elements
        Assert.assertEquals("Wrong length", true, checkVal);
    }
}
