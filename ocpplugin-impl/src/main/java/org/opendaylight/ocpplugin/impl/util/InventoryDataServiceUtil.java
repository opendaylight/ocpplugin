/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.util;

import com.google.common.base.Optional;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeUpdatedBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public abstract class InventoryDataServiceUtil {
    public static final  String OCP_URI_PREFIX = "ocp:";
    private static final Logger LOG = LoggerFactory.getLogger(InventoryDataServiceUtil.class);

    /*
     * Get an InstanceIdentifier for the Nodes class that is the root of the
     * inventory tree We use this a lot, so its worth keeping around
     */
    private static final InstanceIdentifier<Nodes> NODES_IDENTIFIER = InstanceIdentifier.create(Nodes.class);


    public static InstanceIdentifier<Node> identifierFromRadioheadId(final String radioheadId) {
        NodeKey nodeKey = nodeKeyFromRadioheadId(radioheadId);
        return NODES_IDENTIFIER.child(Node.class, nodeKey);
    }

    public static NodeKey nodeKeyFromRadioheadId(final String radioheadId) {
        return new NodeKey(nodeIdFromRadioheadId(radioheadId));
    }

    public static NodeId nodeIdFromRadioheadId(final String radioheadId) {
        return new NodeId(OCP_URI_PREFIX + radioheadId);
    }

    public static String radioheadIdFromNodeId(final NodeId nodeId) {
        String radioheadId = nodeId.getValue().replace(OCP_URI_PREFIX, "");
        return radioheadId;
    }

    public static NodeRef nodeRefFromNodeKey(final NodeKey nodeKey) {
        return new NodeRef(nodeKeyToInstanceIdentifier(nodeKey));
    }

    public static InstanceIdentifier<Node> nodeKeyToInstanceIdentifier(final NodeKey nodeKey) {
        return NODES_IDENTIFIER.child(Node.class, nodeKey);
    }

    public static KeyedInstanceIdentifier<Node, NodeKey> createNodeInstanceIdentifier(NodeId nodeId){
        return InstanceIdentifier.create(Nodes.class).child(Node.class, new NodeKey(nodeId));
    }
}
