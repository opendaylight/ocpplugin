/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.services;

import java.util.concurrent.Future;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContextStack;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.SalObjectStateMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateOutputBuilder;

import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class SalObjectStateMgmtServiceImpl implements SalObjectStateMgmtService {

    private static final Logger LOG = LoggerFactory.getLogger(SalObjectStateMgmtServiceImpl.class);
  
    private final GetState getState;
    private final ModifyState modifyState;

    public SalObjectStateMgmtServiceImpl(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        getState = new GetState(requestContextStack, deviceContext);
        modifyState = new ModifyState(requestContextStack, deviceContext);
    }

    @Override
    public Future<RpcResult<GetStateOutput>> getState(final GetStateInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput>> future = getState.handleServiceCall(input);
        final SettableFuture<RpcResult<GetStateOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput output = result.getResult();
                GetStateOutputBuilder builder = new GetStateOutputBuilder();
                builder.setObj(output.getObj());
                builder.setResult(output.getResult());
                RpcResultBuilder<GetStateOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<GetStateOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

    @Override
    public Future<RpcResult<ModifyStateOutput>> modifyState(final ModifyStateInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput>> future = modifyState.handleServiceCall(input);
        final SettableFuture<RpcResult<ModifyStateOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput output = result.getResult();
                ModifyStateOutputBuilder builder = new ModifyStateOutputBuilder();
                builder.setObj(output.getObj());
                builder.setResult(output.getResult());
                RpcResultBuilder<ModifyStateOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<ModifyStateOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

}
