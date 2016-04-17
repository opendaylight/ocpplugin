/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.extensibility;

import org.opendaylight.yangtools.yang.binding.DataObject;

import io.netty.buffer.ByteBuf;

/**
 * Uniform interface for serializers
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 * @param <T> message type
 */
public interface OCPSerializer <T extends DataObject> extends OCPGeneralSerializer {

    /**
     * Transforms POJO/DTO into byte message (ByteBuf).
     * @param input object to be serialized
     * @param outBuffer output buffer
     */
    void serialize(T input, ByteBuf outBuffer);

}
