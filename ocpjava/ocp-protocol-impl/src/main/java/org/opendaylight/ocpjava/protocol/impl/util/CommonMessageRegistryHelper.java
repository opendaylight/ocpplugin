/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.util;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPGeneralSerializer;
import org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistry;
import org.opendaylight.ocpjava.protocol.api.keys.MessageTypeKey;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class CommonMessageRegistryHelper {

    private short version;
    private SerializerRegistry serializerRegistry;

    /**
     * @param version
     * @param serializerRegistry
     */
    public CommonMessageRegistryHelper(short version, SerializerRegistry serializerRegistry) {
        this.version = version;
        this.serializerRegistry = serializerRegistry;
    }

    /**
     * @param msgType
     * @param serializer
     */
    public void registerSerializer(Class<?> msgType, OCPGeneralSerializer serializer) {
        serializerRegistry.registerSerializer(new MessageTypeKey<>(version, msgType), serializer);
    }
}
