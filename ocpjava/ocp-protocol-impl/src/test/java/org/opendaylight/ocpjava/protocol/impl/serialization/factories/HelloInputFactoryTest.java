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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OriHelloAckRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class HelloInputFactoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(HelloInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<HelloInput> helloInputFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        helloInputFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, HelloInput.class));
    }

    /**
     * Testing of {@link HealthCheckInputMessageFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        HelloInputBuilder hib = new HelloInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("HELLOACK"));
        String testResult = "FAILOCPVERSION";

        
        //set result
        Method m_t = hib.getClass().getMethod("setResult", OriHelloAckRes.class);
        m_t.invoke(hib, OriHelloAckRes.valueOf(testResult));

        //set xid
        Method m2_t = hib.getClass().getMethod("setXid", Long.class);
        m2_t.invoke(hib, new Long(0));

        HelloInput hi = hib.build();
        LOG.debug("HealthCheckInputMessageFactoryTest - hi result value = {}", hi.getResult());    

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        helloInputFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOG.debug("HealthCheckInputMessageFactoryTest - out = {}", out.readableBytes());    
        byte[] bytes = new byte[out.readableBytes()];
        int readerIndex = out.readerIndex();
        out.getBytes(readerIndex, bytes);

        StringBuilder seq = new StringBuilder("");
        seq.append("<result>");
        seq.append(testResult);
        seq.append("</result>");
        String buf = new String(bytes, "UTF-8");
        boolean checkVal = buf.contains(seq);

        //Check and compare elements
        Assert.assertEquals("Wrong length", true, checkVal);
    }
}
