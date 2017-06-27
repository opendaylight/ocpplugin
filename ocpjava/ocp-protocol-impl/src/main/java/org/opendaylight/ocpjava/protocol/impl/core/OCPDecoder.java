/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializationFactory;
import org.opendaylight.ocpjava.statistics.CounterEventTypes;
import org.opendaylight.ocpjava.statistics.OcpStatisticsCounters;
import org.opendaylight.yangtools.yang.binding.DataObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms OCP Protocol messages to POJOs
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class OCPDecoder extends MessageToMessageDecoder<DefaultMessageWrapper> {

    private static final Logger LOG = LoggerFactory.getLogger(OCPDecoder.class);
    private final OcpStatisticsCounters statisticsCounter;

    // TODO: make this final?
    private DeserializationFactory deserializationFactory;

    /**
     * Constructor of class
     */
    public OCPDecoder() {
        LOG.trace("Creating OCP Decoder");
	// TODO: pass as argument
        statisticsCounter = OcpStatisticsCounters.getInstance();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessageWrapper msg,
            List<Object> out) throws Exception {
        
        statisticsCounter.incrementCounter(CounterEventTypes.US_RECEIVED_IN_OCPJAVA);
        if (LOG.isDebugEnabled()) {
            LOG.debug("DefaultMessageWrapper received");
        }

        try {
        	final DataObject dataObject = deserializationFactory.deserialize(msg.getVersionId(), 
                                                                             msg.getTypeId(), 
                                                                             msg.getMessageBuffer());
            if (dataObject == null) {
                LOG.warn("Translated POJO is null");
                statisticsCounter.incrementCounter(CounterEventTypes.US_DECODE_FAIL);
            } else {
                out.add(dataObject);
                statisticsCounter.incrementCounter(CounterEventTypes.US_DECODE_SUCCESS);
            }
        } catch (Exception e) {
            LOG.warn("Message deserialization failed", e);
            statisticsCounter.incrementCounter(CounterEventTypes.US_DECODE_FAIL);
        } finally {
            msg.getMessageBuffer().clear();
        }
    }

    /**
     * @param deserializationFactory
     */
    public void setDeserializationFactory(DeserializationFactory deserializationFactory) {
        this.deserializationFactory = deserializationFactory;
    }

}
