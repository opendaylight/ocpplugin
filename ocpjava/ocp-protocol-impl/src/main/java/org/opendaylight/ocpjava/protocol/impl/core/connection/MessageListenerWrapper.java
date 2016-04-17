/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;

/**
 * Wraps outgoing message and includes listener attached to this message. This object
 * is sent to OCPEncoder. When OCPEncoder fails to serialize the message,
 * listener is filled with exception. The exception is then delegated to upper ODL layers.
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class MessageListenerWrapper {

    private OcpHeader msg;
    private GenericFutureListener<Future<Void>> listener;

    /**
     * @param msg outgoing message
     * @param listener listener attached to channel.write(msg) Future
     */
    public MessageListenerWrapper(Object msg, GenericFutureListener<Future<Void>> listener) {
        this.msg = (OcpHeader) msg;
        this.listener = listener;
    }

    /**
     * @return outgoing message (downstream)
     */
    public OcpHeader getMsg() {
        return msg;
    }


    /**
     * @return listener listening on message sending success / failure
     */
    public GenericFutureListener<Future<Void>> getListener() {
        return listener;
    }
}
