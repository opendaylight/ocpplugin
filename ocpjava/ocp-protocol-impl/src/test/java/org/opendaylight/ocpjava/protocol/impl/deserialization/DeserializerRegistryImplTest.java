/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpjava.protocol.impl.deserialization;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.keys.MessageCodeKey;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput;

/**
 *
 * @author madamjak
 *
 */
public class DeserializerRegistryImplTest {

    private static final short OCP = EncodeConstants.OCP_VERSION_ID;
    private static final int EMPTY_VALUE = EncodeConstants.EMPTY_VALUE;

    /**
     * Test - register deserializer without arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterDeserializerNoArgs(){
        DeserializerRegistryImpl serReg = new DeserializerRegistryImpl();
        serReg.registerDeserializer(null, null);
    }


    /**
     * Test - register deserializer with no deserializer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterDeserializerNoDeserializer(){
        DeserializerRegistryImpl serReg = new DeserializerRegistryImpl();
        serReg.registerDeserializer(new MessageCodeKey(OCP, EMPTY_VALUE, HealthCheckOutput.class), null);
    }

    /**
     * Test - unregister deserializer without MessageTypeKey
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnRegisterDeserializerNoMessageTypeKey(){
        DeserializerRegistryImpl derserReg = new DeserializerRegistryImpl();
        derserReg.init();
        derserReg.unregisterDeserializer(null);
    }

    /**
     * Test - unregister deserializer
     */
    @Test
    public void testUnRegisterDeserializer(){
        DeserializerRegistryImpl derserReg = new DeserializerRegistryImpl();
        derserReg.init();
        Assert.assertTrue("Wrong - unregister serializer",derserReg.unregisterDeserializer(new MessageCodeKey(OCP,1, HealthCheckOutput.class)));
        Assert.assertFalse("Wrong - unregister serializer",derserReg.unregisterDeserializer(new MessageCodeKey(OCP,EMPTY_VALUE, HealthCheckOutput.class)));
    }

    /**
     * Test - get deserializer
     */
    @Test(expected=IllegalStateException.class)
    public void testGetDeserializer(){
        DeserializerRegistryImpl registry = new DeserializerRegistryImpl();
        registry.init();
        registry.getDeserializer(new MessageCodeKey((short) 5000, EncodeConstants.EMPTY_VALUE, HealthCheckOutput.class));
        Assert.fail();
    }
}
