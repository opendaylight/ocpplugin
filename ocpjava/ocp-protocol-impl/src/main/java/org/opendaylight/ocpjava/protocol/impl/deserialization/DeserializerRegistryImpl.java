/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization;

import java.util.HashMap;
import java.util.Map;

import org.opendaylight.ocpjava.protocol.api.extensibility.DeserializerRegistry;
import org.opendaylight.ocpjava.protocol.api.extensibility.DeserializerRegistryInjector;
import org.opendaylight.ocpjava.protocol.api.extensibility.OCPGeneralDeserializer;
import org.opendaylight.ocpjava.protocol.api.keys.MessageCodeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Stores and registers deserializers
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 * 
 */
public class DeserializerRegistryImpl implements DeserializerRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(DeserializerRegistryImpl.class);
    private Map<MessageCodeKey, OCPGeneralDeserializer> registry;

    /**
     * Decoder table provisioning
     */
    @Override
    public void init() {
        registry = new HashMap<>();
        // register message deserializers
        MessageDeserializerInitializer.registerMessageDeserializers(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends OCPGeneralDeserializer> T getDeserializer(
            MessageCodeKey key) {
    	
        LOG.trace("DeserializerRegistryImpl - getDeserializer: key = {}", key.toString());

        OCPGeneralDeserializer deserializer = registry.get(key);
        if (deserializer == null) {
            throw new IllegalStateException("Deserializer for key: " + key
                    + " was not found - please verify that all needed deserializers ale loaded correctly");
        }
        return (T) deserializer;
    }

    @Override
    public void registerDeserializer(MessageCodeKey key,
            OCPGeneralDeserializer deserializer) {
        if ((key == null) || (deserializer == null)) {
            throw new IllegalArgumentException("MessageCodeKey or Deserializer is null");
        }
        OCPGeneralDeserializer desInRegistry = registry.put(key, deserializer);
        if (desInRegistry != null) {
            LOG.debug("Deserializer for key {} overwritten. Old deserializer: {}, new deserializer: {}",
                    key, desInRegistry.getClass().getName(), deserializer.getClass().getName());
        }
        if (deserializer instanceof DeserializerRegistryInjector) {
            ((DeserializerRegistryInjector) deserializer).injectDeserializerRegistry(this);
        }
    }

    @Override
    public boolean unregisterDeserializer(MessageCodeKey key) {
        if (key == null) {
            throw new IllegalArgumentException("MessageCodeKey is null");
        }
        OCPGeneralDeserializer deserializer = registry.remove(key);
        if (deserializer == null) {
            return false;
        }
        return true;
    }

}
