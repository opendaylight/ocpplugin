/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.extensibility;

import org.opendaylight.ocpjava.protocol.api.keys.MessageCodeKey;


/**
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public interface DeserializerRegistry {

    /**
     * Initializes deserializers
     */
    void init();

    /**
     * @param key used for deserializer lookup
     * @return deserializer found
     */
    <T extends OCPGeneralDeserializer>
            T getDeserializer(MessageCodeKey key);

    /**
     * Registers deserializer.
     * Throws IllegalStateException when there is
     * a deserializer already registered under given key.
     *
     * If the deserializer implements {@link DeserializerRegistryInjector} interface,
     * the deserializer is injected with DeserializerRegistry instance.
     *
     * @param key used for deserializer lookup
     * @param deserializer deserializer instance
     */
    void registerDeserializer(MessageCodeKey key,
            OCPGeneralDeserializer deserializer);

    /**
     * Unregisters deserializer
     * @param key used for deserializer lookup
     * @return true if deserializer was removed,
     *  false if no deserializer was found under specified key
     */
    boolean unregisterDeserializer(MessageCodeKey key);
}
