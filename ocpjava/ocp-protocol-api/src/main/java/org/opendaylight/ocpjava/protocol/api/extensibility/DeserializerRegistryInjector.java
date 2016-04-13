/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.extensibility;

/**
 * Injects registry
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public interface DeserializerRegistryInjector {

    /**
     * Injects deserializer registry into deserializer
     * @param deserializerRegistry
     */
    void injectDeserializerRegistry(DeserializerRegistry deserializerRegistry);
}
