/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection.listener;

import com.google.common.base.Objects;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeContext;
import org.opendaylight.ocpplugin.impl.connection.HandshakeStepWrapper;
import org.opendaylight.ocpplugin.api.ocp.device.listener.OcpMessageListenerFacade;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChange;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class OcpProtocolListenerInitialImpl implements OcpMessageListenerFacade {

    private static final Logger LOG = LoggerFactory.getLogger(OcpProtocolListenerInitialImpl.class);

    private final ConnectionContext connectionContext;
    private final HandshakeContext handshakeContext;

    /**
     * @param connectionContext
     * @param handshakeContext
     */
    public OcpProtocolListenerInitialImpl(final ConnectionContext connectionContext,
                                          final HandshakeContext handshakeContext) {
        this.connectionContext = connectionContext;
        this.handshakeContext = handshakeContext;
    }

    @Override
    public void onFaultInd(final FaultInd faultInd) {
        // FIXME: invalid state - must disconnect and close all contexts
    }

    @Override
    public void onStateChange(final StateChange stateChange) {
        // FIXME: invalid state - must disconnect and close all contexts
    }

    @Override
    public void onHelloMessage(final HelloMessage hello) {
        LOG.debug("processing HELLO.xid: {}", hello.getXid());
        if (connectionContext.getConnectionState() == null) {
            connectionContext.changeStateToHelloWait();
        }

        if (checkState(ConnectionContext.CONNECTION_STATE.HELLO_WAIT)) {
            final HandshakeStepWrapper handshakeStepWrapper = new HandshakeStepWrapper(
                    hello, handshakeContext.getHandshakeManager(), connectionContext.getConnectionAdapter());
            handshakeStepWrapper.run();
        } else {
            //TODO: consider disconnecting of bad behaving device
        }
    }

    /**
     * @param expectedState
     */
    protected boolean checkState(final ConnectionContext.CONNECTION_STATE expectedState) {
        boolean verdict = true;
        if (! Objects.equal(connectionContext.getConnectionState(), expectedState)) {
            verdict = false;
            LOG.info("Expected state: {}, actual state: {}", expectedState,
                    connectionContext.getConnectionState());
        }

        return verdict;
    }
}
