/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.serialization;

import org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistry;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;

import org.opendaylight.ocpjava.protocol.impl.serialization.factories.HealthCheckInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.SetTimeInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.ReResetInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.GetParamInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.GetStateInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.GetFaultInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.ModifyParamInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.CreateObjInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.DeleteObjInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.ModifyStateInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.HelloInputFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.factories.ReDirectInputFactory;

import org.opendaylight.ocpjava.protocol.impl.util.CommonMessageRegistryHelper;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.ReDirectInput;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public final class MessageFactoryInitializer {

    private MessageFactoryInitializer() {
        throw new UnsupportedOperationException("Utility class shouldn't be instantiated");
    }

    /**
     * Registers message serializers into provided registry
     * @param serializerRegistry registry to be initialized with message serializers
     */
    public static void registerMessageSerializers(SerializerRegistry serializerRegistry) {
        // register OCP message serializers
        short version = 1;
        CommonMessageRegistryHelper registryHelper = new CommonMessageRegistryHelper(version, serializerRegistry);
        registryHelper.registerSerializer(HealthCheckInput.class, new HealthCheckInputFactory());
        registryHelper.registerSerializer(SetTimeInput.class, new SetTimeInputFactory());
        registryHelper.registerSerializer(ReResetInput.class, new ReResetInputFactory());
        registryHelper.registerSerializer(GetParamInput.class, new GetParamInputFactory());
        registryHelper.registerSerializer(CreateObjInput.class, new CreateObjInputFactory());
        registryHelper.registerSerializer(DeleteObjInput.class, new DeleteObjInputFactory());
        registryHelper.registerSerializer(GetStateInput.class, new GetStateInputFactory());
        registryHelper.registerSerializer(GetFaultInput.class, new GetFaultInputFactory());
        registryHelper.registerSerializer(ModifyParamInput.class, new ModifyParamInputFactory());
        registryHelper.registerSerializer(ModifyStateInput.class, new ModifyStateInputFactory());
        registryHelper.registerSerializer(HelloInput.class, new HelloInputFactory());
        registryHelper.registerSerializer(ReDirectInput.class, new ReDirectInputFactory());
    }
}
