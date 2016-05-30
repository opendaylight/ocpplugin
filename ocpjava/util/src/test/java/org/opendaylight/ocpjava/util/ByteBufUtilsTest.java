/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.util.ByteBufUtils;

/**
 * @author michal.polkorab
 *
 */
public class ByteBufUtilsTest {

    private byte[] expected = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, (byte) 0xff};

    /**
     * Test of {@link org.opendaylight.ocpjava.util.ByteBufUtils#hexStringToBytes(String)}
     */
    @Test
    public void testHexStringToBytes() {
        byte[] data = ByteBufUtils.hexStringToBytes("01 02 03 04 05 ff");

        Assert.assertArrayEquals(expected, data);
    }

    /**
     * Test of {@link ByteBufUtils#hexStringToBytes(String, boolean)}
     */
    @Test
    public void testHexStringToBytes2() {
        byte[] data = ByteBufUtils.hexStringToBytes("0102030405ff", false);

        Assert.assertArrayEquals(expected, data);
    }
    
    /**
     * Test bytes to hex string
     */
    @Test
    public void testBytesToHexString() {
        byte[] array = new byte[]{10, 11, 12, 13, 14, 15, 16};
        Assert.assertEquals("Wrong conversion", "0a 0b 0c 0d 0e 0f 10", ByteBufUtils.bytesToHexString(array));
        byte[] empty = new byte[0];
        Assert.assertEquals("Wrong conversion", "", ByteBufUtils.bytesToHexString(empty));
    }
}
