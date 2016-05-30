/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.clients;

import java.util.Arrays;

import org.opendaylight.ocpjava.util.ByteBufUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class representing waiting on message
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class WaitForMessageEvent implements ClientEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitForMessageEvent.class);
    private byte[] messageExpected;
    private byte[] messageReceived;

    /**
     * @param expactation of received message
     */
    public WaitForMessageEvent(byte[] messageExpected) {
        LOGGER.debug("WaitForMessageEvent init");
        LOGGER.debug("headerExpected length: {}", messageExpected.length);

        this.messageExpected = new byte[messageExpected.length];
        for (int i = 0; i < messageExpected.length; i++) {
            this.messageExpected[i] = messageExpected[i];
        }
    }

    @Override
    public boolean eventExecuted() {
        if (messageReceived == null) {
            return false;
        }
        if (!Arrays.equals(messageExpected, messageReceived)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("expected msg: {}", ByteBufUtils.bytesToHexString(messageExpected));
                LOGGER.debug("received msg: {}", ByteBufUtils.bytesToHexString(messageReceived));
            }
            return false;
        }
        LOGGER.debug("Headers OK");
        return true;
    }

    /**
     * @param expactation of received message
     */
    public void setHeaderReceived(byte[] messageReceived) {
        this.messageReceived = new byte[messageReceived.length];
        for (int i = 0; i < messageReceived.length; i++) {
            this.messageReceived[i] = messageReceived[i];
        }
    }
}
