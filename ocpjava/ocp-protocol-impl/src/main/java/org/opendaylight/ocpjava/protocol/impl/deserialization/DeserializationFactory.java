/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Map;
import org.opendaylight.ocpjava.protocol.api.extensibility.DeserializerRegistry;
import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;
import org.opendaylight.ocpjava.protocol.api.keys.MessageCodeKey;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.protocol.impl.deserialization.factories.HelloMessageFactory;
import org.opendaylight.ocpjava.protocol.impl.util.TypeToClassKey;
import org.opendaylight.yangtools.yang.binding.DataObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author michal.polkorab
 * @author timotej.kubas
 * @author giuseppex.petralia@intel.com
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class DeserializationFactory {

    private final Map<TypeToClassKey, Class<?>> messageClassMap;
    private DeserializerRegistry registry;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeserializationFactory.class);


    /**
     * Constructor
     */
    public DeserializationFactory() {
        final Map<TypeToClassKey, Class<?>> temp = new HashMap<>();
        TypeToClassMapInitializer.initializeTypeToClassMap(temp);
        messageClassMap = ImmutableMap.copyOf(temp);
        LOGGER.trace("DeserializationFactory: messageClassMap = " + messageClassMap); 
    }

    /**
     * Transforms List<Object> into correct POJO message
     * @param rawMessage
     * @param version version decoded from OCP protocol message
     * @return correct POJO as DataObject
     */

    public DataObject deserialize(final short version, final int type, final List<Object> rawMessage){
        DataObject dataObject = null;
        Class<?> clazz = messageClassMap.get(new TypeToClassKey(version, type));

        OCPDeserializer<DataObject> deserializer = registry.getDeserializer(
                new MessageCodeKey(version, type, clazz));
        dataObject = deserializer.deserialize(rawMessage);
        return dataObject;
    }

    /**
     * @param registry
     */
    public void setRegistry(final DeserializerRegistry registry) {
        this.registry = registry;
    }

}
