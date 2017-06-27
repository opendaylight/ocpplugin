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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdDateTime;

import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class SetTimeInputFactoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(SetTimeInputFactoryTest.class);

    private SerializerRegistry registry;
    private OCPSerializer<SetTimeInput> setTimeInputFactory;

    /**
     * Initializes serializer registry and stores correct factory in field
     */
    @Before
    public void startUp() {
        registry = new SerializerRegistryImpl();
        registry.init();
        setTimeInputFactory = registry.getSerializer(
                new MessageTypeKey<>(EncodeConstants.OCP_VERSION_ID, SetTimeInput.class));
    }

    /**
     * Testing of {@link SetTimeInputFactory} for correct translation from POJO
     * @throws Exception
     */
    @Test
    public void testElementsSet() throws Exception {
        SetTimeInputBuilder hib = new SetTimeInputBuilder();
        BufferHelper.setupHeader(hib, OcpMsgType.valueOf("SETTIMEREQ"));
        String testNewTime = "2016-04-26T10:23:00-05:00";
        
        //set tcpLinkMonTimeout
        Method m_t = hib.getClass().getMethod("setNewTime", XsdDateTime.class);
        m_t.invoke(hib, new XsdDateTime(testNewTime));

        //set xid
        Method m2_t = hib.getClass().getMethod("setXid", Long.class);
        m2_t.invoke(hib, new Long(0));

        SetTimeInput hi = hib.build();
        LOG.debug("HealthCheckInputMessageFactoryTest - hi testNewTime value = {}", hi.getNewTime().getValue().toString());    

        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        setTimeInputFactory.serialize(hi, out);

        //Verify and check the bytebuf info
        LOG.debug("SetTimeInputFactoryTest - out = {}", out.readableBytes());    
        byte[] bytes = new byte[out.readableBytes()];
        int readerIndex = out.readerIndex();
        out.getBytes(readerIndex, bytes);

        StringBuilder seq = new StringBuilder("");
        seq.append("<newTime>");
        seq.append(testNewTime);
        seq.append("</newTime>");
        String buf = new String(bytes, "UTF-8");
        boolean checkVal = buf.contains(seq);

        //Check and compare elements
        Assert.assertEquals("Wrong length", true, checkVal);
    }
}
