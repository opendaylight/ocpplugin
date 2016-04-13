/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.extensibility;

import io.netty.buffer.ByteBuf;

import org.opendaylight.yangtools.yang.binding.DataObject;

import java.util.List;

/**
 * Uniform interface for deserializing factories
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 * @param <E> message code type
 */
public interface OCPDeserializer<E extends DataObject> extends OCPGeneralDeserializer {

    /**
     * Transforms List<Object> message into POJO/DTO (of type E).
     *
     * @param message message as List<Object>
     * @return POJO/DTO
     */
    E deserialize(List<Object> message);
}
