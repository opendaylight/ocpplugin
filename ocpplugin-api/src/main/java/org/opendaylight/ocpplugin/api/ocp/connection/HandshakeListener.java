/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.connection;

import org.opendaylight.ocpplugin.api.ocp.connection.HandshakeContext;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;

/*
 * @author mirehak
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public interface HandshakeListener {

    /**
     * @param Hello obtained
     */
    void onHandshakeSuccessfull(HelloMessage hello);

    /**
     * This method is called when handshake fails for some reason. It allows
     * all necessary cleanup operations.
     */
    void onHandshakeFailure(ConnectionContext.CONNECTION_STATE nextState);

    /**
     * @param handshakeContext
     */
    void setHandshakeContext(HandshakeContext handshakeContext);
}
