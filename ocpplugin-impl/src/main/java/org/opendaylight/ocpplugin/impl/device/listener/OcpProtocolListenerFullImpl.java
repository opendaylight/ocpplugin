/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.device.listener;

import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceReplyProcessor;
import org.opendaylight.ocpplugin.api.ocp.device.listener.OcpMessageListenerFacade;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChangeInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class OcpProtocolListenerFullImpl implements OcpMessageListenerFacade {

    private static final Logger LOG = LoggerFactory.getLogger(OcpProtocolListenerFullImpl.class);

    private final ConnectionAdapter connectionAdapter;
    private final DeviceReplyProcessor deviceReplyProcessor;

    /**
     * @param connectionAdapter
     * @param deviceReplyProcessor
     */
    public OcpProtocolListenerFullImpl(final ConnectionAdapter connectionAdapter, final DeviceReplyProcessor deviceReplyProcessor) {
        this.connectionAdapter = connectionAdapter;
        this.deviceReplyProcessor = deviceReplyProcessor;
    }

    @Override
    public void onFaultInd(final FaultInd faultInd) {
        deviceReplyProcessor.processFaultIndication(faultInd);
    }

    @Override
    public void onStateChangeInd(final StateChangeInd stateChange) {
        deviceReplyProcessor.processStateChange(stateChange); 
    }

    @Override
    public void onHelloMessage(final HelloMessage hello) {
        // FIXME: invalid state - must disconnect and close all contexts
    }

}
