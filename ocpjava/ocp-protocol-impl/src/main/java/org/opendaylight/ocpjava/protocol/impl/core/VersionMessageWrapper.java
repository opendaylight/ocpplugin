/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import java.util.List;

/**
 * Wraps received messages (includes version)
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public class VersionMessageWrapper {

    private short version;
    private List<Object> messageBuffer;

    /**
     * Constructor
     * @param version version decoded in {@link OCPVersionDetector}
     * @param messageBuffer message received from {@link OCPXmlDecoder}
     */
    public VersionMessageWrapper(short version, List<Object> messageBuffer) {
        this.version = version;
        this.messageBuffer = messageBuffer;
    }

    /**
     * @return the version version decoded in {@link OCPVersionDetector}
     */
    public short getVersion() {
        return version;
    }

    /**
     * @return the messageBuffer message received from {@link OCPXmlDecoder}
     */
    public List<Object> getMessageBuffer() {
        return messageBuffer;
    }


}
