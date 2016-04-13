/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.ErrorHandler;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeListener;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeManager;
import org.opendaylight.ocpplugin.api.OcpConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpMsgType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OriHelloAckRes;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author mirehak
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class HandshakeManagerImpl implements HandshakeManager {

    private static final Logger LOG = LoggerFactory.getLogger(HandshakeManagerImpl.class);

    private final ConnectionAdapter connectionAdapter;
    private ErrorHandler errorHandler;
    private HandshakeListener handshakeListener;


    /**
     * @param connectionAdapter
     */
    public HandshakeManagerImpl(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    @Override
    public void setHandshakeListener(HandshakeListener handshakeListener) {
        this.handshakeListener = handshakeListener;
    }

    @Override
    public synchronized void shake(HelloMessage receivedHello) {

        LOG.trace("handshake STARTED");

        try {
            if (receivedHello == null) {
                LOG.trace("ret - awaiting hello message");
                return;
            }

            LOG.debug("Hello message: xid={}, version={}", receivedHello.getXid(), receivedHello.getVersion());

            HelloInputBuilder builder = new HelloInputBuilder();
            builder.setMsgType(OcpMsgType.HELLOIND);
            builder.setXid(receivedHello.getXid());
 
            if (receivedHello.getVersion().equals(OcpConstants.OCP_VERSION)) {
                LOG.trace("ret - shake success");
                builder.setResult(OriHelloAckRes.SUCCESS);
                connectionAdapter.hello(builder.build());
                handshakeListener.onHandshakeSuccessfull(receivedHello);
            }
            else {
                LOG.trace("ret - shake fail - version mismatch");
                builder.setResult(OriHelloAckRes.FAILOCPVERSION);
                connectionAdapter.hello(builder.build());
                handshakeListener.onHandshakeFailure(ConnectionContext.CONNECTION_STATE.MAINTENANCE);
            }
       } catch (Exception ex) {
            errorHandler.handleException(ex);
            LOG.trace("ret - shake fail - closing");
            handshakeListener.onHandshakeFailure(ConnectionContext.CONNECTION_STATE.CLOSED);
        }
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
