/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation 
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection;

import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.opendaylight.controller.sal.binding.api.RpcConsumerRegistry;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionReadyListener;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionManager;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeContext;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceConnectedHandler;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeListener;
import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeManager;
import org.opendaylight.ocpplugin.impl.connection.listener.ConnectionReadyListenerImpl;
import org.opendaylight.ocpplugin.impl.connection.listener.HandshakeListenerImpl;
import org.opendaylight.ocpplugin.impl.connection.listener.OcpProtocolListenerInitialImpl;
import org.opendaylight.ocpplugin.impl.connection.listener.SystemNotificationsListenerImpl;
import org.opendaylight.ocpplugin.impl.connection.ErrorHandlerSimpleImpl;
import org.opendaylight.ocpplugin.impl.connection.HandshakeManagerImpl;
import org.opendaylight.ocpplugin.impl.common.ThreadPoolLoggingExecutor;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.OcpProtocolListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.SystemNotificationsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class ConnectionManagerImpl implements ConnectionManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManagerImpl.class);
    private static final int HELLO_LIMIT = 20;
    private final RpcConsumerRegistry rpcRegistry;
    private DeviceConnectedHandler deviceConnectedHandler;

    public ConnectionManagerImpl(@Nonnull final RpcConsumerRegistry rpcRegistry) {
        this.rpcRegistry = rpcRegistry;
    }

    @Override
    public void onRadioHeadConnected(final ConnectionAdapter connectionAdapter) {

        LOG.trace("preparing handshake: {}", connectionAdapter.getRemoteAddress());

        final int handshakeThreadLimit = 1; //TODO: move to constants/parametrize
        final ThreadPoolLoggingExecutor handshakePool = createHandshakePool(
                connectionAdapter.getRemoteAddress().toString(), handshakeThreadLimit);

        LOG.trace("prepare connection context");
        final ConnectionContext connectionContext = new ConnectionContextImpl(connectionAdapter);

        HandshakeListener handshakeListener = new HandshakeListenerImpl(connectionContext, deviceConnectedHandler);
        final HandshakeManager handshakeManager = createHandshakeManager(connectionAdapter, handshakeListener);

        LOG.trace("prepare handshake context");
        HandshakeContext handshakeContext = new HandshakeContextImpl(handshakePool, handshakeManager);
        handshakeListener.setHandshakeContext(handshakeContext);

        LOG.trace("prepare connection listeners");
        final ConnectionReadyListener connectionReadyListener = new ConnectionReadyListenerImpl(
                connectionContext, handshakeContext);
        connectionAdapter.setConnectionReadyListener(connectionReadyListener);

        final OcpProtocolListenerInitialImpl ocpMessageListener =
                new OcpProtocolListenerInitialImpl(connectionContext, handshakeContext);
        connectionAdapter.setMessageListener(ocpMessageListener);
        connectionAdapter.setMessageExtListener(ocpMessageListener);

        final SystemNotificationsListener systemListener = new SystemNotificationsListenerImpl(connectionContext, rpcRegistry);
        connectionAdapter.setSystemListener(systemListener);

        LOG.trace("connection ballet finished");
    }

    /**
     * @param connectionIdentifier
     * @param handshakeThreadLimit
     * @return
     */
    private static ThreadPoolLoggingExecutor createHandshakePool(
            final String connectionIdentifier, final int handshakeThreadLimit) {
        return new ThreadPoolLoggingExecutor(handshakeThreadLimit,
                handshakeThreadLimit, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(HELLO_LIMIT), "OCPHandshake-" + connectionIdentifier);
    }

    /**
     * @param connectionAdapter
     * @param handshakeListener
     * @return
     */
    private HandshakeManager createHandshakeManager(final ConnectionAdapter connectionAdapter,
                                                    final HandshakeListener handshakeListener) {
        HandshakeManagerImpl handshakeManager = new HandshakeManagerImpl(connectionAdapter);
        handshakeManager.setHandshakeListener(handshakeListener);
        handshakeManager.setErrorHandler(new ErrorHandlerSimpleImpl());

        return handshakeManager;
    }

    @Override
    public boolean accept(final InetAddress radioHeadAddress) {
        // TODO add connection accept logic based on address
        return true;
    }

    @Override
    public void setDeviceConnectedHandler(final DeviceConnectedHandler deviceConnectedHandler) {
        this.deviceConnectedHandler = deviceConnectedHandler;
    }

}
