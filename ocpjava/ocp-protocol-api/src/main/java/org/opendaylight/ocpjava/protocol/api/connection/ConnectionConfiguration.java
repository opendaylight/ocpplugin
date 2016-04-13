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
public interface ConnectionConfiguration {

    /**
     * @return address to bind, if null, all available interfaces will be used
     */
    InetAddress getAddress();

    /**
     * @return port to bind
     */
    int getPort();

    /**
     * @return transport protocol to use
     */
    Object getTransferProtocol();

    /**
     * @return TLS configuration object
     */
    TlsConfiguration getTlsConfiguration();

    /**
     * @return silence time (in milliseconds) - after this time
     *         {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.RadioHeadIdleEvent}
     *         message is sent upstream
     */
    long getRadioHeadIdleTimeout();

    /**
     * @return seed for {@link javax.net.ssl.SSLEngine}
     */
    Object getSslContext();

    /**
     * @return thread numbers for TcpHandler's eventloopGroups
     */
    ThreadConfiguration getThreadConfiguration();
}
