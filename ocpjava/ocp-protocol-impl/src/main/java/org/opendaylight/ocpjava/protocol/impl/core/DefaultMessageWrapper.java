/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;


import java.util.List;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;

/**
 * Wraps received messages
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class DefaultMessageWrapper {

    private short versionId;
    private int typeId;
    private List<Object> messageBuffer;

    /**
     * Constructor
     * @param messageBuffer message received from {@link OCPXmlDecoder}

     */
    public DefaultMessageWrapper(short versionId, int typeId, List<Object> messageBuffer) {
        this.messageBuffer = messageBuffer;
        this.versionId = versionId;
        this.typeId = typeId;

    }

    /**
     * @return the messageBuffer message received from {@link OCPXmlDecoder}
     */
    public List<Object> getMessageBuffer() {
        return messageBuffer;
    }

    public short getVersionId() {
        return versionId;
    }

    public int getTypeId() {
        return typeId;
    }
}
