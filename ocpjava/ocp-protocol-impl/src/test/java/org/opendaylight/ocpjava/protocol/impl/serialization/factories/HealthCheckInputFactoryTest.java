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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class HealthCheckInputFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<HealthCheckInput> healthCheckFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        healthCheckFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, HealthCheckInput.class));
    }

    /**
     * Testing of {@link HealthCheckInputMessageFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        HealthCheckInputBuilder hib = new HealthCheckInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("HEALTHCHECKREQ"));
        int testTimeout = 16;
        
        //set tcpLinkMonTimeout
        Method m_t = hib.getClass().getMethod("setTcpLinkMonTimeout", XsdUnsignedShort.class);
        m_t.invoke(hib, new XsdUnsignedShort(testTimeout));

        //set xid
        Method m2_t = hib.getClass().getMethod("setXid", Long.class);
        m2_t.invoke(hib, new Long(0));

        HealthCheckInput hi = hib.build();
        LOGGER.debug("HealthCheckInputMessageFactoryTest - hi testTimeout value = {}", hi.getTcpLinkMonTimeout().getValue());    

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        healthCheckFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOGGER.debug("HealthCheckInputMessageFactoryTest - out = {}", out.readableBytes());    
        byte[] bytes = new byte[out.readableBytes()];
        int readerIndex = out.readerIndex();
        out.getBytes(readerIndex, bytes);

        StringBuilder seq = new StringBuilder("");
        seq.append("<tcpLinkMonTimeout>");
        seq.append(testTimeout);
        seq.append("</tcpLinkMonTimeout>");
        String buf = new String(bytes, "UTF-8");
        boolean checkVal = buf.contains(seq);

        //Check and compare elements
        Assert.assertEquals("Wrong length", true, checkVal);
    }
}
