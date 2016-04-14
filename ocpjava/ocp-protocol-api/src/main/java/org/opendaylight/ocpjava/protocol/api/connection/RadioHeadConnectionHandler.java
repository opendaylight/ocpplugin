/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.connection;

import java.net.InetAddress;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public interface RadioHeadConnectionHandler {

    /**
     * @param connection to radioHead proving message sending/receiving, connection management
     */
    void onRadioHeadConnected(ConnectionAdapter connection);

    /**
     * @param radioHeadAddress
     * @return true, if connection from RadioHead having given address shell be accepted; false otherwise
     */
    boolean accept(InetAddress radioHeadAddress);

}
