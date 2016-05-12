/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization;

import java.util.Map;

import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.protocol.impl.util.TypeToClassInitHelper;
import org.opendaylight.ocpjava.protocol.impl.util.TypeToClassKey;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.ReDirectOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChange;


/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public final class TypeToClassMapInitializer {

    private TypeToClassMapInitializer() {
        throw new UnsupportedOperationException("Utility class shouldn't be instantiated");
    }

    /**
     * Initializes type to class map
     * @param messageClassMap
     */
    public static void initializeTypeToClassMap(Map<TypeToClassKey, Class<?>> messageClassMap) {
        // init OCP v4.1.1 mapping
        TypeToClassInitHelper helper =
                new TypeToClassInitHelper(EncodeConstants.OCP_VERSION_ID, messageClassMap);
        helper.registerTypeToClass( 1, HealthCheckOutput.class);
        helper.registerTypeToClass( 3, SetTimeOutput.class);
        helper.registerTypeToClass( 5, ReResetOutput.class);
        helper.registerTypeToClass( 7, GetParamOutput.class);
        helper.registerTypeToClass( 9, ModifyParamOutput.class);
        helper.registerTypeToClass( 11, CreateObjOutput.class);
        helper.registerTypeToClass( 13, DeleteObjOutput.class);
        helper.registerTypeToClass( 15, GetStateOutput.class);
        helper.registerTypeToClass( 16, StateChange.class);
        helper.registerTypeToClass( 18, ModifyStateOutput.class);
        helper.registerTypeToClass( 20, GetFaultOutput.class);
        helper.registerTypeToClass( 21, FaultInd.class);
        helper.registerTypeToClass( 23, HelloMessage.class);
    }
}
