/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.List;

/** Class for common operations on ByteBuf
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public abstract class ByteBufUtils {
    private static final Splitter HEXSTRING_SPLITTER =  Splitter.onPattern("\\s+").omitEmptyStrings();
    private static final Splitter HEXSTRING_NOSPACE_SPLITTER = Splitter.onPattern("(?<=\\G.{2})").omitEmptyStrings();

    private ByteBufUtils() {
        //not called
    }

    /**
     * Converts String into byte[]
     * @param hexSrc input String
     * @return byte[] filled with input data
     */
    public static byte[] hexStringToBytes(final String hexSrc) {
        return hexStringToBytes(hexSrc, true);
    }

    /**
     * Converts String into byte[]
     * @param hexSrc input String
     * @param withSpaces if there are spaces in string
     * @return byte[] filled with input data
     */
    public static byte[] hexStringToBytes(final String hexSrc, final boolean withSpaces) {
        final Splitter splitter = withSpaces ? HEXSTRING_SPLITTER : HEXSTRING_NOSPACE_SPLITTER;
        List<String> byteChips = Lists.newArrayList(splitter.split(hexSrc));
        byte[] result = new byte[byteChips.size()];
        int i = 0;
        for (String chip : byteChips) {
            result[i] = (byte) Short.parseShort(chip, 16);
            i++;
        }
        return result;
    }

    /**
     * Converts byte array into String
     * @param array input byte array
     * @return String
     */
    public static String bytesToHexString(final byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte element : array) {
            sb.append(String.format(" %02x", element));
        }
        return sb.toString().trim();
    }
}
