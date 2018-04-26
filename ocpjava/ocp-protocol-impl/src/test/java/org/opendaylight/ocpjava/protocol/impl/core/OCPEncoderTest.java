/*
 * Copyright (c) 2014 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyShort;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.protocol.impl.core.connection.MessageListenerWrapper;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializationFactory;
import org.opendaylight.yangtools.yang.binding.DataObject;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;

/**
 *
 * @author jameshall
 */
public class OCPEncoderTest {

    @Mock ChannelHandlerContext mockChHndlrCtx ;
    @Mock SerializationFactory mockSerializationFactory ;
    @Mock MessageListenerWrapper wrapper;
    @Mock OcpHeader mockMsg ;
    @Mock ByteBuf mockOut ;
    @Mock Future<Void> future;
    @Mock GenericFutureListener<Future<Void>> listener;

    OCPEncoder ocpEncoder = new OCPEncoder() ;

    /**
     * Sets up test environment
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ocpEncoder = new OCPEncoder() ;
        ocpEncoder.setSerializationFactory( mockSerializationFactory ) ;
    }

    /**
     * Test successful write (no clear)
     */
    @Test
    public void testEncodeSuccess() {
        when(mockOut.readableBytes()).thenReturn(1);
        when(wrapper.getMsg()).thenReturn(mockMsg);
        try {
            ocpEncoder.encode(mockChHndlrCtx, wrapper, mockOut);
        } catch (Exception e) {
            Assert.fail();
        }

        // Verify that the channel was flushed after the ByteBuf was retained.
        verify(mockOut, times(0)).clear();
    }

    /**
     * Test Bytebuf clearing after serialization failure
     */
    @Test
    public void testEncodeSerializationException() {
        when(wrapper.getMsg()).thenReturn(mockMsg);
        when(wrapper.getListener()).thenReturn(listener);
        doThrow(new IllegalArgumentException()).when(mockSerializationFactory).messageToBuffer(anyShort(),any(ByteBuf.class), any(DataObject.class));
        try {
            ocpEncoder.encode(mockChHndlrCtx, wrapper, mockOut);
        } catch (Exception e) {
            Assert.fail();
        }

        // Verify that the output message buf was cleared...
        verify(mockOut, times(1)).clear();
    }

    /**
     * Test no action on empty bytebuf
     */
    @Test
    public void testEncodeSerializesNoBytes() {
        when(mockOut.readableBytes()).thenReturn(0);
        when(wrapper.getMsg()).thenReturn(mockMsg);
        try {
            ocpEncoder.encode(mockChHndlrCtx, wrapper, mockOut);
        } catch (Exception e) {
            Assert.fail();
        }

        // Verify that the output message buf was cleared...
        verify(mockOut, times(0)).clear();
        verify(mockChHndlrCtx, times(0)).writeAndFlush(mockOut);
        verify(mockOut, times(0)).retain();
    }
}