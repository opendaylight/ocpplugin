/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.serialization;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistry;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;

/**
 * @author michal.polkorab
 *
 */
public class SerializationFactoryTest {

    /**
     * Test serializer lookup & serialization
     */
    @Test
    public void test() {
        SerializerRegistry registry = new SerializerRegistryImpl();
        registry.init();
        SerializationFactory factory = new SerializationFactory();
        factory.setSerializerTable(registry);
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer();
        HealthCheckInputBuilder healthcheckBuilder = new HealthCheckInputBuilder();
        healthcheckBuilder.setMsgType(OcpMsgType.valueOf("HEALTHCHECKREQ"));
        healthcheckBuilder.setXid((long) 1234);
        healthcheckBuilder.setTcpLinkMonTimeout(new XsdUnsignedShort(5));
        factory.messageToBuffer((short)1, buffer, healthcheckBuilder.build());
    }

    /**
     * Test serializer not found scenario
     */
    @Test(expected=IllegalStateException.class)
    public void testNotExistingSerializer() {
        SerializerRegistry registry = new SerializerRegistryImpl();
        registry.init();
        SerializationFactory factory = new SerializationFactory();
        factory.setSerializerTable(registry);
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer();
        HealthCheckInputBuilder healthcheckBuilder = new HealthCheckInputBuilder();
        healthcheckBuilder.setMsgType(OcpMsgType.valueOf("HEALTHCHECKREQ"));
        healthcheckBuilder.setXid((long) 1234);
        healthcheckBuilder.setTcpLinkMonTimeout(new XsdUnsignedShort(5));        
        factory.messageToBuffer((short)22, buffer, healthcheckBuilder.build());
    }
}
