/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.protocol.impl.util.TypeToClassKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChange;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.ReDirectOutput;


/**
 * @author Marko, Foxconn
 *
 */
public class TypeToClassMapInitializerTest {

    private Map<TypeToClassKey, Class<?>> messageClassMap;

    /**
     * Tests correct map initialization
     */
    @Test
    public void test() {
        messageClassMap = new HashMap<>();
        TypeToClassMapInitializer.initializeTypeToClassMap(messageClassMap);
        short version = EncodeConstants.OCP_VERSION_ID;
        assertEquals("Wrong class", HealthCheckOutput.class, messageClassMap.get(new TypeToClassKey(version, 1)));
        assertEquals("Wrong class", SetTimeOutput.class, messageClassMap.get(new TypeToClassKey(version, 3)));
        assertEquals("Wrong class", ReResetOutput.class, messageClassMap.get(new TypeToClassKey(version, 5)));
        assertEquals("Wrong class", GetParamOutput.class, messageClassMap.get(new TypeToClassKey(version, 7)));
        assertEquals("Wrong class", ModifyParamOutput.class, messageClassMap.get(new TypeToClassKey(version, 9)));
        assertEquals("Wrong class", CreateObjOutput.class, messageClassMap.get(new TypeToClassKey(version, 11)));
        assertEquals("Wrong class", DeleteObjOutput.class, messageClassMap.get(new TypeToClassKey(version, 13)));
        assertEquals("Wrong class", GetStateOutput.class, messageClassMap.get(new TypeToClassKey(version, 15)));
        assertEquals("Wrong class", StateChange.class, messageClassMap.get(new TypeToClassKey(version, 16)));
        assertEquals("Wrong class", ModifyStateOutput.class, messageClassMap.get(new TypeToClassKey(version, 18)));
        assertEquals("Wrong class", GetFaultOutput.class, messageClassMap.get(new TypeToClassKey(version, 20)));
        assertEquals("Wrong class", FaultInd.class, messageClassMap.get(new TypeToClassKey(version, 21)));
        assertEquals("Wrong class", HelloMessage.class, messageClassMap.get(new TypeToClassKey(version, 23)));
    }
}
