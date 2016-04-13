/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection;

import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeManager;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author mirehak
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class HandshakeStepWrapper implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(HandshakeStepWrapper.class);

    private HelloMessage helloMessage;
    private HandshakeManager handshakeManager;
    private ConnectionAdapter connectionAdapter;


    /**
     * @param helloMessage
     * @param handshakeManager
     * @param connectionAdapter
     */
    public HandshakeStepWrapper(HelloMessage helloMessage,
            HandshakeManager handshakeManager, ConnectionAdapter connectionAdapter) {
        this.helloMessage = helloMessage;
        this.handshakeManager = handshakeManager;
        this.connectionAdapter = connectionAdapter;
    }

    @Override
    public void run() {
        if (connectionAdapter.isAlive()) {
            handshakeManager.shake(helloMessage);
        } else {
            LOG.debug("connection is down - skipping handshake step");
        }
    }

}
