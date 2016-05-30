/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.Future;

import org.opendaylight.ocpjava.protocol.impl.core.connection.MessageListenerWrapper;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializationFactory;
import org.opendaylight.ocpjava.statistics.CounterEventTypes;
import org.opendaylight.ocpjava.statistics.OcpStatisticsCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms OCP Protocol messages to POJOs
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class OCPEncoder extends MessageToByteEncoder<MessageListenerWrapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCPEncoder.class);
    private SerializationFactory serializationFactory;
    private OcpStatisticsCounters statisticsCounters;

    /** Constructor of class */
    public OCPEncoder() {
        statisticsCounters = OcpStatisticsCounters.getInstance();
        LOGGER.trace("Creating OCPEncoder");
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageListenerWrapper wrapper, ByteBuf out)
            throws Exception {
        LOGGER.trace("Encoding");
        try {
            serializationFactory.messageToBuffer((short)1, out, wrapper.getMsg());
            statisticsCounters.incrementCounter(CounterEventTypes.DS_ENCODE_SUCCESS);
        } catch(Exception e) {
            LOGGER.warn("Message serialization failed ", e);
            statisticsCounters.incrementCounter(CounterEventTypes.DS_ENCODE_FAIL);
            Future<Void> newFailedFuture = ctx.newFailedFuture(e);
            wrapper.getListener().operationComplete(newFailedFuture);
            out.clear();
            return;
        }
    }

    /**
     * @param serializationFactory
     */
    public void setSerializationFactory(SerializationFactory serializationFactory) {
        this.serializationFactory = serializationFactory;
    }

}
