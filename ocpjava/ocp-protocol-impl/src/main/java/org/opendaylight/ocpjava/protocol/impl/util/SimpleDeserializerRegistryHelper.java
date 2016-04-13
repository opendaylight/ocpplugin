/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.util;

import org.opendaylight.ocpjava.protocol.api.extensibility.DeserializerRegistry;
import org.opendaylight.ocpjava.protocol.api.extensibility.OCPGeneralDeserializer;
import org.opendaylight.ocpjava.protocol.api.keys.MessageCodeKey;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class SimpleDeserializerRegistryHelper {

    private short version;
    private DeserializerRegistry registry;

    /**
     * @param version wire protocol version
     * @param deserializerRegistry registry to be filled with message deserializers
     */
    public SimpleDeserializerRegistryHelper(short version, DeserializerRegistry deserializerRegistry) {
        this.version = version;
        this.registry = deserializerRegistry;
    }

    /**
     * @param code code / value to distinguish between deserializers
     * @param experimenterID TODO
     * @param deserializedObjectClass class of object that will be deserialized
     *  by given deserializer
     * @param deserializer deserializer instance
     */
    public void registerDeserializer(int code,
            Long experimenterID, Class<?> deserializedObjectClass, OCPGeneralDeserializer deserializer) {
        registry.registerDeserializer(new MessageCodeKey(version, code,
                deserializedObjectClass), deserializer);
    }
}
