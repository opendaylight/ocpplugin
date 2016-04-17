/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.device;

import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceManager;

public class ReadyForNewTransactionChainHandlerImpl implements ReadyForNewTransactionChainHandler {

    private final DeviceManager deviceManager;
    private final ConnectionContext connectionContext;

    public ReadyForNewTransactionChainHandlerImpl(final DeviceManager deviceManager, final ConnectionContext connectionContext) {
        this.deviceManager = deviceManager;
        this.connectionContext = connectionContext;
    }

    @Override
    public void onReadyForNewTransactionChain() {
        deviceManager.deviceConnected(connectionContext);
    }
}
