/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2016 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.List;
import org.junit.Assert;
import org.opendaylight.ocpjava.protocol.api.extensibility.OCPDeserializer;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.util.ByteBufUtils;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yangtools.yang.binding.DataObject;

/**
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public abstract class BufferHelper {

    /**
     *
     */
    public static final Long DEFAULT_XID = 0L;
    //public static final Long DEFAULT_XID = 0x01020304L;


    private static final byte[] XID = new byte[] { 0x01, 0x02, 0x03, 0x04 };

    /**
     * @param payload
     * @return ByteBuf filled with OCP protocol message without first 4
     *         bytes
     */
    public static ByteBuf buildBuffer(byte[] payload) {
        ByteBuf bb = UnpooledByteBufAllocator.DEFAULT.buffer();
        bb.writeBytes(XID);
        bb.writeBytes(payload);
        return bb;
    }

    /**
     * @param payload String in hex format
     * @return ByteBuf filled with OCP protocol message without first 4
     *         bytes
     */
    public static ByteBuf buildBuffer(String payload) {
        return buildBuffer(ByteBufUtils.hexStringToBytes(payload));
    }

    /**
     * @return ByteBuf filled with OpenFlow protocol header message without first 4
     *         bytes
     */
    public static ByteBuf buildBuffer() {
        ByteBuf bb = UnpooledByteBufAllocator.DEFAULT.buffer();
        bb.writeBytes(XID);
        bb.writeBytes(new byte[0]);
        return bb;
    }

    /**
     * Use version 1.0 for encoded message
     * @param input ByteBuf to be checked for correct OCP Protocol header
     * @param msgType type of received message
     * @param length expected length of message in header
     */
    public static void checkHeaderV10(ByteBuf input, byte msgType) {
        checkHeader(input, msgType, (short) EncodeConstants.OCP_VERSION_ID);
    }

    private static void checkHeader(ByteBuf input, byte msgType, Short version) {
        Assert.assertEquals("Wrong version", version, Short.valueOf(input.readByte()));
        Assert.assertEquals("Wrong type", msgType, input.readByte());
        Assert.assertEquals("Wrong Xid", DEFAULT_XID, Long.valueOf(input.readUnsignedInt()));
    }


    /**
     * @param ocpHeader OCP protocol header
     */
    public static void checkHeaderV10(OcpHeader ocpHeader) {
        checkHeader(ocpHeader);
    }

    private static void checkHeader(OcpHeader ocpHeader) {
        Assert.assertEquals("Wrong Xid", DEFAULT_XID, ocpHeader.getXid());
    }

    /**
     * @param builder
     * @param type wire protocol number used
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void setupHeader(Object builder, OcpMsgType type) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method m = builder.getClass().getMethod("setMsgType", OcpMsgType.class);
        m.invoke(builder, (OcpMsgType) type);
        Method m2 = builder.getClass().getMethod("setXid", Long.class);
        m2.invoke(builder, BufferHelper.DEFAULT_XID);
    }

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
