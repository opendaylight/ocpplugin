/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.connection;

import com.google.common.annotations.Beta;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.OcpProtocolService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.OcpProtocolListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.SystemNotificationsListener;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public interface ConnectionAdapter extends OcpProtocolService, OcpExtensionService {

    /**
     * disconnect corresponding RRH
     * @return future set to true, when disconnect completed
     */
    Future<Boolean> disconnect();

    /**
     * @return true, if connection to RRH is alive
     */
    boolean isAlive();

    /**
     * @return address of the remote end - address of a RRH if connected
     */
    InetSocketAddress getRemoteAddress();

    /**
     * @param messageListener here will be pushed all messages from RRH
     */
    void setMessageListener(OcpProtocolListener messageListener);

    /**
     * marko.lai added for OCP extension Indication, 2015/07/29
     * @param messageListener here will be pushed all messages from RRH
     */    
    void setMessageExtListener(OcpExtensionListener messageExtListener);


    /**
     * @param systemListener here will be pushed all system messages from library
     */
    void setSystemListener(SystemNotificationsListener systemListener);

    /**
     * Throws exception if any of required listeners is missing
     */
    void checkListeners();

    /**
     * notify listener about connection ready-to-use event
     */
    void fireConnectionReadyNotification();

    /**
     * set listener for connection became ready-to-use event
     * @param connectionReadyListener
     */
    void setConnectionReadyListener(ConnectionReadyListener connectionReadyListener);

    /**
     * sets option for automatic channel reading;
     * if set to false, incoming messages won't be read
     */
    void setAutoRead(boolean autoRead);

    /**
     * @return true, if channel is configured to autoread
     */
    boolean isAutoRead();

    /**
     * marko.lai added, 2015/11/11
     * sets option for idle timeout millisecond;
     */
    void setIdleTimeout(long idleTimeout);

    /**
     * Registers a new bypass outbound queue
     * @param handler
     * @param maxQueueDepth
     * @return An {@link OutboundQueueHandlerRegistration}
     */
    @Beta
    <T extends OutboundQueueHandler> OutboundQueueHandlerRegistration<T> registerOutboundQueueHandler(T handler,
        int maxQueueDepth);

}
