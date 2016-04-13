/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection.listener;

import org.opendaylight.ocpjava.protocol.api.connection.ConnectionReadyListener;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeContext;
import org.opendaylight.ocpplugin.impl.connection.HandshakeStepWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * oneshot listener - once connection is ready, initiate handshake (if not already started by device)
 */
public class ConnectionReadyListenerImpl implements ConnectionReadyListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionReadyListenerImpl.class);

    private ConnectionContext connectionContext;
    private HandshakeContext handshakeContext;

    /**
     * @param connectionContext
     * @param handshakeContext
     */
    public ConnectionReadyListenerImpl(ConnectionContext connectionContext,
            HandshakeContext handshakeContext) {
                this.connectionContext = connectionContext;
                this.handshakeContext = handshakeContext;
    }

    @Override
    public void onConnectionReady() {
        LOG.debug("device is connected and ready-to-use (pipeline prepared): {}",
                connectionContext.getConnectionAdapter().getRemoteAddress());

        if (connectionContext.getConnectionState() == null) {
            HandshakeStepWrapper handshakeStepWrapper = new HandshakeStepWrapper(
                    null, handshakeContext.getHandshakeManager(), connectionContext.getConnectionAdapter());
            handshakeContext.getHandshakePool().execute(handshakeStepWrapper);
            connectionContext.changeStateToHelloWait();
        } else {
            LOG.debug("already touched by hello message");
        }
    }

}
