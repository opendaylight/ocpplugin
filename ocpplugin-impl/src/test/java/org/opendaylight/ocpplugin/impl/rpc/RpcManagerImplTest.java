/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.rpc;

import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.ocpplugin.api.ocp.connection.ConnectionContext;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceState;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceInitializationPhaseHandler;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContext;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.RpcService;

@RunWith(MockitoJUnitRunner.class)
public class RpcManagerImplTest {

    private static final int AWAITED_NUM_OF_CALL_ADD_ROUTED_RPC = 5;

    private RpcManagerImpl rpcManager;
    @Mock
    private ProviderContext rpcProviderRegistry;
    @Mock
    private DeviceContext deviceContext;
    @Mock
    private DeviceInitializationPhaseHandler deviceINitializationPhaseHandler;
    @Mock
    private ConnectionContext connectionContext;
    @Mock
    private BindingAwareBroker.RoutedRpcRegistration<RpcService> routedRpcRegistration;
    @Mock
    private DeviceState deviceState;

    private KeyedInstanceIdentifier<Node, NodeKey> nodePath;

    @Before
    public void setUp() {
        final NodeKey nodeKey = new NodeKey(new NodeId("ocp-junit:1"));
        nodePath = KeyedInstanceIdentifier.create(Nodes.class).child(Node.class, nodeKey);
        rpcManager = new RpcManagerImpl(rpcProviderRegistry, 5);
        rpcManager.setDeviceInitializationPhaseHandler(deviceINitializationPhaseHandler);
        Mockito.when(deviceContext.getDeviceState()).thenReturn(deviceState);
        Mockito.when(deviceState.getNodeInstanceIdentifier()).thenReturn(nodePath);
        Mockito.when(deviceState.getNodeId()).thenReturn(nodeKey.getId());
    }

    @Test
    public void testOnDeviceContextLevelUp() throws Exception {

        Mockito.when(rpcProviderRegistry.addRoutedRpcImplementation(
                Matchers.<Class<RpcService>>any(), Matchers.any(RpcService.class)))
                .thenReturn(routedRpcRegistration);

        rpcManager.onDeviceContextLevelUp(deviceContext);

        Mockito.verify(rpcProviderRegistry, times(AWAITED_NUM_OF_CALL_ADD_ROUTED_RPC)).addRoutedRpcImplementation(
                Matchers.<Class<RpcService>>any(), Matchers.any(RpcService.class));
        Mockito.verify(routedRpcRegistration, times(AWAITED_NUM_OF_CALL_ADD_ROUTED_RPC)).registerPath(
                NodeContext.class, nodePath);
        Mockito.verify(deviceINitializationPhaseHandler).onDeviceContextLevelUp(deviceContext);
    }
}
