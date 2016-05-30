/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.serialization;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.keys.MessageTypeKey;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;

import org.opendaylight.ocpjava.protocol.impl.serialization.factories.HealthCheckInputFactory;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;

/**
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class SerializerRegistryImplTest {

    private static final short OCP = EncodeConstants.OCP_VERSION_ID;
    private static final short FAILED_VER = EncodeConstants.UNKNOWN_VER_ID;

    /**
     * Test - register serializer without arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSerializerNoArgs(){

        SerializerRegistryImpl serReg = new SerializerRegistryImpl();
        serReg.registerSerializer(null, null);
    }

    /**
     * Test - unregister serializer without MessageTypeKye
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnRegisterSerializerNoMessageTypeKey(){
        SerializerRegistryImpl serReg = new SerializerRegistryImpl();
        serReg.init();
        serReg.registerSerializer(new MessageTypeKey<>(OCP, HealthCheckInput.class), new HealthCheckInputFactory());
        serReg.unregisterSerializer(null);
    }

    /**
     * Test - unregister serializer
     */
    @Test
    public void testUnRegisterSerializer(){
        SerializerRegistryImpl serReg = new SerializerRegistryImpl();
        serReg.init();
        serReg.registerSerializer(new MessageTypeKey<>(OCP, HealthCheckInput.class), new HealthCheckInputFactory());
        Assert.assertTrue("Wrong - unregister serializer",serReg.unregisterSerializer(new MessageTypeKey<>(OCP, HealthCheckInput.class)));

        serReg.registerSerializer(new MessageTypeKey<>(OCP, HealthCheckInput.class), new HealthCheckInputFactory());
        Assert.assertFalse("Wrong - unregister serializer",serReg.unregisterSerializer(new MessageTypeKey<>(FAILED_VER, HealthCheckInput.class)));
    }   
}
