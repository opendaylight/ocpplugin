/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */


package org.opendaylight.ocpjava.protocol.impl.clients;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for decoding incoming messages into message frames.
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class SimpleClientFramer extends ByteToMessageDecoder {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleClientFramer.class);

    private List<Object> out;    
    private String buf = "";
    
    /**
     * Constructor of class.
     */
    public SimpleClientFramer() {
        LOG.debug("Creating OCPFrameDecoder");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.warn("Unexpected exception from downstream.", cause);
        ctx.close();
    }

    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readableBytes();
        byte[] bs = new byte[len];
        in.readBytes(bs);
        out.add(bs);
    }
}
