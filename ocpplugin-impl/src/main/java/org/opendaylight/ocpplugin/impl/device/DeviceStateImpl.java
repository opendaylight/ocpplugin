/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation 
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.device;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceState;
import org.opendaylight.ocpplugin.impl.util.InventoryDataServiceUtil;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;

/*
 * DeviceState is built from NodeId, whose value is inside
 * org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext
 *
 * @author Vaclav Demcak
 * @author Richard Chien <richard.chien@foxconn.com>
 */
class DeviceStateImpl implements DeviceState {

    private final NodeId nodeId;
    private final KeyedInstanceIdentifier<Node, NodeKey> nodeII;
    private boolean valid;

    public DeviceStateImpl(@Nonnull final NodeId nodeId) {
        this.nodeId = Preconditions.checkNotNull(nodeId);
        nodeII = InventoryDataServiceUtil.createNodeInstanceIdentifier(nodeId);
    }

    @Override
    public NodeId getNodeId() {
        return nodeId;
    }

    @Override
    public KeyedInstanceIdentifier<Node, NodeKey> getNodeInstanceIdentifier() {
        return nodeII;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setValid(final boolean valid) {
        this.valid = valid;
    }

}
