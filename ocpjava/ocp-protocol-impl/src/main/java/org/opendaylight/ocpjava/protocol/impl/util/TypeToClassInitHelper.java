/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class TypeToClassInitHelper {

    private short version;
    private Map<TypeToClassKey, Class<?>> messageClassMap;
    private static final Logger LOG = LoggerFactory.getLogger(TypeToClassInitHelper.class);

    /**
     * Constructor
     * @param version protocol wire version
     * @param messageClassMap map which stores type to class mapping
     */
    public TypeToClassInitHelper(short version, Map<TypeToClassKey,
            Class<?>> messageClassMap) {
        this.version = version;
        this.messageClassMap = messageClassMap;
    }

    /**
     * Registers Class int the type to class mapping
     * @param type code value for message type / class
     * @param clazz class corresponding to the code
     */
    public void registerTypeToClass(int type, Class<?> clazz) {
        LOG.info("registerTypeToClass: clazz(string) = {}", clazz.getName());
        messageClassMap.put(new TypeToClassKey(version, type), clazz);
    }
}
