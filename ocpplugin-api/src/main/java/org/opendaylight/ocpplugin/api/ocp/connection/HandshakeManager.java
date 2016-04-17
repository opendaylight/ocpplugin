/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.connection;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;

public interface HandshakeManager {

    /**
     * @param errorHandler the errorHandler to set
     */
    void setErrorHandler(ErrorHandler errorHandler);

    /**
     * @param handshakeListener the handshakeListener to set
     */
    void setHandshakeListener(HandshakeListener handshakeListener);

    /**
     * @param receivedHello message from device we need to act upon
     * process current handshake step
     */
    void shake(HelloMessage receivedHello);
}
