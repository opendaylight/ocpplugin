/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

/**
 * Stores names of handlers used in pipeline.
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public enum PipelineHandlers {

    /**
     * Detects radioHead idle state
     */
    IDLE_HANDLER,
    /**
     * Component for handling TLS frames
     */
    SSL_HANDLER,
    /**
     * Decodes incoming messages into message frames
     */
    OCP_XML_DECODER,
    /**
     * Transforms OCP Protocol byte messages into POJOs
     */
    OCP_DECODER,
    /**
     * Transforms POJOs into OCP Protocol byte messages
     */
    OCP_ENCODER,
    /**
     * Delegates translated POJOs into MessageConsumer
     */
    DELEGATING_INBOUND_HANDLER,
    /**
     * Performs efficient flushing
     */
    CHANNEL_OUTBOUNF_QUEUE
}
