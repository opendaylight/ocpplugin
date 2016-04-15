/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.util;

/**
 * Stores common constants
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public abstract class EncodeConstants {

    /** OCP v4.1.1 Version wire protocol number */
	public static final short OCP_VERSION_ID = 1; //given hash value
    /** Length of mac address */
    public static final byte MAC_ADDRESS_LENGTH = 6;
    /** Number of groups in ipv4 address */
    public static final byte GROUPS_IN_IPV4_ADDRESS = 4;
    /** Number of groups in ipv6 address */
    public static final byte GROUPS_IN_IPV6_ADDRESS = 8;
    /** Length of ipv6 address in bytes */
    public static final byte SIZE_OF_IPV6_ADDRESS_IN_BYTES = (8 * Short.SIZE) / Byte.SIZE;
    /** Length of byte in bytes */
    public static final byte SIZE_OF_BYTE_IN_BYTES = Byte.SIZE / Byte.SIZE;
    /** Empty (zero) int value */
    public static final int EMPTY_VALUE = 0;

    private EncodeConstants() {
        //not called
    }
}
