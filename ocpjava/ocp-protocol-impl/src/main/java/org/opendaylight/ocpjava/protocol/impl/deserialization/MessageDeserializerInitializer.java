/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization;

import org.opendaylight.ocpjava.protocol.api.extensibility.DeserializerRegistry;

import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.HealthCheckOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.SetTimeOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.ReResetOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.GetParamOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.GetStateOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.GetFaultOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.FaultIndFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.ModifyParamOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.CreateObjOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.DeleteObjOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.ModifyStateOutputFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.StateChangeFactory;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.HelloMessageFactory;

import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.protocol.impl.util.SimpleDeserializerRegistryHelper;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChange;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public final class MessageDeserializerInitializer {

    private MessageDeserializerInitializer() {
        throw new UnsupportedOperationException("Utility class shouldn't be instantiated");
    }

    /**
     * Registers message deserializers
     * @param registry registry to be filled with deserializers
     */
    public static void registerMessageDeserializers(DeserializerRegistry registry) {
        // register OCP v4.1.1 message deserializers
        SimpleDeserializerRegistryHelper helper =
                new SimpleDeserializerRegistryHelper(EncodeConstants.OCP_VERSION_ID, registry);

        helper.registerDeserializer(1, null, HealthCheckOutput.class, new HealthCheckOutputFactory());
        helper.registerDeserializer(3, null, SetTimeOutput.class, new SetTimeOutputFactory());
        helper.registerDeserializer(5, null, ReResetOutput.class, new ReResetOutputFactory());
        helper.registerDeserializer(7, null, GetParamOutput.class, new GetParamOutputFactory());
        helper.registerDeserializer(9, null, ModifyParamOutput.class, new ModifyParamOutputFactory());
        helper.registerDeserializer(11, null, CreateObjOutput.class, new CreateObjOutputFactory());
        helper.registerDeserializer(13, null, DeleteObjOutput.class, new DeleteObjOutputFactory());        
        helper.registerDeserializer(15, null, GetStateOutput.class, new GetStateOutputFactory());
        helper.registerDeserializer(16, null, StateChange.class, new StateChangeFactory());
        helper.registerDeserializer(18, null, ModifyStateOutput.class, new ModifyStateOutputFactory());
        helper.registerDeserializer(20, null, GetFaultOutput.class, new GetFaultOutputFactory());
        helper.registerDeserializer(21, null, FaultInd.class, new FaultIndFactory());
        helper.registerDeserializer(23, null, HelloMessage.class, new HelloMessageFactory());
    }
}
