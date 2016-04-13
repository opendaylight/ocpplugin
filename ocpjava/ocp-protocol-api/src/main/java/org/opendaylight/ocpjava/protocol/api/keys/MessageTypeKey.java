/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.keys;

/**
 * Class used as a key in {@link org.opendaylight.ocpjava.protocol.api.extensibility.SerializerRegistry}
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 * @param <E> message type (class)
 */
public class MessageTypeKey<E> {

    private final Class<? extends E> msgType;
    private final short msgVersion;

    /**
     * @param msgVersion protocol version
     * @param msgType type of message - class of serialized object
     */
    public MessageTypeKey(short msgVersion, Class<? extends E> msgType) {
        this.msgType = msgType;
        this.msgVersion = msgVersion;
    }

    @Override
    public String toString() {
        return "msgVersion: " + msgVersion + " objectType: " + msgType.getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((msgType == null) ? 0 : msgType.hashCode());
        result = prime * result + msgVersion;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MessageTypeKey)) {
            return false;
        }
        MessageTypeKey<?> other = (MessageTypeKey<?>) obj;
        if (msgType == null) {
            if (other.msgType != null) {
                return false;
            }
        } else if (!msgType.equals(other.msgType)) {
            return false;
        }
        if (msgVersion != other.msgVersion) {
            return false;
        }
        return true;
    }
}
