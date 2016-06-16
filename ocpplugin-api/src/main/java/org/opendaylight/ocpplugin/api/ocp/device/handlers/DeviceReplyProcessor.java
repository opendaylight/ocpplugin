/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.device.handlers;

import java.util.List;
import org.opendaylight.ocpplugin.api.ocp.device.Xid;
import org.opendaylight.ocpplugin.api.ocp.device.listener.OcpMessageListenerFacade;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChangeInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public interface DeviceReplyProcessor {

    /**
     * Method process fault indication from device
     *
     * @param errorMessage
     */
    public void processFaultIndication(FaultInd faultInd);

    /**
     * Method process state change notification from device
     *
     * @param errorMessage
     */
    public void processStateChange(StateChangeInd stateChange);

}
