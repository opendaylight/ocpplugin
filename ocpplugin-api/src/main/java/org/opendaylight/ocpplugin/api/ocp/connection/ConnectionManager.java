/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.connection;

import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceConnectedHandler;

/**
 * Connection manager manages connections with devices.
 * It instantiates and registers ConnectionContext
 * used for handling all communication with device when onRadioHeadConnected notification is processed.
 */
public interface ConnectionManager extends RadioHeadConnectionHandler {

    /**
     * Method registers handler responsible handling operations related to connected device after
     * device is connected.
     *
     * @param deviceConnectedHandler
     */
    void setDeviceConnectedHandler(DeviceConnectedHandler deviceConnectedHandler);

}
