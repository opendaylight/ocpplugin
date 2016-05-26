/*
 * Copyright (c) 2015 Foxconn Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.device;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;

/*
 * test of DeviceStateImpl
 *
 * @author Richard Chien <richard.chien@foxconn.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class DeviceStateImplTest {

    private NodeId nodeId;
    private DeviceStateImpl deviceState;

    @Before
    public void initialization() {
        nodeId = new NodeId("ocp:1");
        deviceState = new DeviceStateImpl(nodeId);
    }

    /**
     * Test method for DeviceStateImpl#DeviceStateImpl(NodeId).
     */
    @Test(expected=NullPointerException.class)
    public void testDeviceStateImplNullNodeId(){
        new DeviceStateImpl(null);
    }

    /**
     * Test method for DeviceStateImpl#getNodeId().
     */
    @Test
    public void testGetNodeId(){
        final NodeId getNodeId = deviceState.getNodeId();
        Assert.assertNotNull(getNodeId);
        Assert.assertEquals(nodeId, getNodeId);
    }

    @Test
    public void testIsValid_initialValue(){
        Assert.assertFalse(deviceState.isValid());
    }

}
