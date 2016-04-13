/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.serialization;

import java.util.HashMap;
import java.util.Map;

import org.opendaylight.ocpjava.protocol.api.extensibility.OCPGeneralSerializer;
import org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistry;
import org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistryInjector;
import org.opendaylight.ocpjava.protocol.api.keys.MessageTypeKey;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores and handles serializers<br>
 * K - {@link MessageTypeKey} type<br>
 * S - returned serializer type
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class SerializerRegistryImpl implements SerializerRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializerRegistryImpl.class);
    private Map<MessageTypeKey<?>, OCPGeneralSerializer> registry;


    @Override
    public void init() {
        registry = new HashMap<>();
        // OCP message type serializers
        MessageFactoryInitializer.registerMessageSerializers(this);
    }

    /**
     * @param msgTypeKey
     * @return encoder for current type of message (msgTypeKey)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <K, S extends OCPGeneralSerializer> S getSerializer(
            MessageTypeKey<K> msgTypeKey) {
        OCPGeneralSerializer serializer = registry.get(msgTypeKey);
        if (serializer == null) {
            throw new IllegalStateException("Serializer for key: " + msgTypeKey
                    + " was not found - please verify that you are using correct message"
                    + " combination (e.g. OCP v4.1.1 message to OCP v4.1.1 device)");
        }
        return (S) serializer;
    }

    @Override
    public <K> void registerSerializer(
            MessageTypeKey<K> msgTypeKey, OCPGeneralSerializer serializer) {
        if ((msgTypeKey == null) || (serializer == null)) {
            throw new IllegalArgumentException("MessageTypeKey or Serializer is null");
        }
        OCPGeneralSerializer serInRegistry = registry.put(msgTypeKey, serializer);
        if (serInRegistry != null) {
            LOGGER.debug("Serializer for key {} overwritten. Old serializer: {}, new serializer: {}",
                    msgTypeKey, serInRegistry.getClass().getName(), serializer.getClass().getName());
        }
        if (serializer instanceof SerializerRegistryInjector) {
            ((SerializerRegistryInjector) serializer).injectSerializerRegistry(this);
        }
    }

    @Override
    public <K> boolean unregisterSerializer(MessageTypeKey<K> msgTypeKey) {
        if (msgTypeKey == null) {
            throw new IllegalArgumentException("MessageTypeKey is null");
        }
        OCPGeneralSerializer serializer = registry.remove(msgTypeKey);
        if (serializer == null) {
            return false;
        }
        return true;
    }
}
