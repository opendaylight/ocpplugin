/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.extensibility;

import org.opendaylight.ocpjava.protocol.api.keys.MessageTypeKey;

/**
 * Stores and handles serializers<br>
 * K - {@link MessageTypeKey} parameter type,<br>
 * S - returned serializer type
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public interface SerializerRegistry {

    /**
     * Serializer registry provisioning
     */
    void init();

    /**
     * @param msgTypeKey lookup key
     * @return serializer or NullPointerException if no serializer was found
     */
    <K, S extends OCPGeneralSerializer> S getSerializer(MessageTypeKey<K> msgTypeKey);

    /**
     * Registers serializer
     * Throws IllegalStateException when there is
     * a serializer already registered under given key.
     *
     * If the serializer implements {@link SerializerRegistryInjector} interface,
     * the serializer is injected with SerializerRegistry instance.
     *
     * @param key used for serializer lookup
     * @param serializer serializer implementation
     */
    <K> void registerSerializer(MessageTypeKey<K> key,
            OCPGeneralSerializer serializer);

    /**
     * Unregisters serializer
     * @param key used for serializer lookup
     * @return true if serializer was removed,
     *  false if no serializer was found under specified key
     */
    <K> boolean unregisterSerializer(MessageTypeKey<K> key);
}
