/*
 * Copyright (c) 2015 Foxconn Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.device.listener;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceReplyProcessor;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.OcpProtocolListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultIndBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChangeInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChangeIndBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessageBuilder;

/*
 * test of OcpProtocolListenerFullImpl
 *
 * @author Richard Chien <richard.chien@foxconn.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class OcpProtocolListenerFullImplTest {

    private OcpProtocolListenerFullImpl ocpProtocolListener;

    @Mock
    private DeviceReplyProcessor deviceReplyProcessor;
    @Mock
    private ConnectionAdapter connectionAdapter;

    private final long xid = 42L;

    @Before
    public void setUp() {
        // place for mocking method's general behavior for HandshakeContext and ConnectionContext
        ocpProtocolListener = new OcpProtocolListenerFullImpl(connectionAdapter, deviceReplyProcessor);
        connectionAdapter.setMessageListener(ocpProtocolListener);
        connectionAdapter.setMessageExtListener(ocpProtocolListener);
        Mockito.verify(connectionAdapter).setMessageListener(Matchers.any(OcpProtocolListener.class));
        Mockito.verify(connectionAdapter).setMessageExtListener(Matchers.any(OcpExtensionListener.class));
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(connectionAdapter, deviceReplyProcessor);
    }

    /**
     * Test method for OcpProtocolListenerFullImpl#onFaultInd(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd).
     */
    @Test
    public void testOnFaultInd() {
        FaultInd faultInd = new FaultIndBuilder().setXid(xid).build();
        ocpProtocolListener.onFaultInd(faultInd);

        Mockito.verify(deviceReplyProcessor).processFaultIndication(Matchers.any(FaultInd.class));
    }

    /**
     * Test method for OcpProtocolListenerFullImpl#onStateChange(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChange).
     */
    @Test
    public void testOnStateChangeInd() {
        StateChangeInd stateChangeInd = new StateChangeIndBuilder().setXid(xid).build();
        ocpProtocolListener.onStateChangeInd(stateChangeInd);

        Mockito.verify(deviceReplyProcessor).processStateChange(Matchers.any(StateChangeInd.class));
    }

    /**
     * Test method for OcpProtocolListenerFullImpl#onHelloMessage(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage).
     */
    @Test
    public void testOnHelloMessage() {
        HelloMessage hello = new HelloMessageBuilder().setXid(xid).build();
        ocpProtocolListener.onHelloMessage(hello);
        // NOOP
    }

}
