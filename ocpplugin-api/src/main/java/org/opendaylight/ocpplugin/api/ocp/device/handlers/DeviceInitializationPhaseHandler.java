/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpplugin.api.ocp.device.handlers;

import javax.annotation.CheckForNull;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;

/*
 * Represents handler for new connected device building cycle. Every
 * implementation has some necessary steps which have to be done before
 * adding new device to MD-SAL DataStore.
 */

public interface DeviceInitializationPhaseHandler {

    /**
     * Method represents an initialization cycle for {@link DeviceContext}
     * preparation for use.
     *
     * @param deviceContext
     */
    void onDeviceContextLevelUp(@CheckForNull DeviceContext deviceContext);
}
