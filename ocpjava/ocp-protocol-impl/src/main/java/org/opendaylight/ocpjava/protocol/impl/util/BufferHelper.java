/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.util;

import io.netty.buffer.ByteBuf;

import java.util.List;
import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;
import org.opendaylight.yangtools.yang.binding.DataObject;

/**
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public abstract class BufferHelper {

    /**
     * Decode message
     * @param decoder decoder instance
     * @param bb data input buffer
     * @return message decoded pojo
     */
    public static <E extends DataObject> E deserialize(OCPDeserializer<E> decoder, List<Object> bb) {
        return decoder.deserialize(bb);
    }

}
