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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.SalObjectLifecycleService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutputBuilder;

import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class SalObjectLifecycleServiceImpl implements SalObjectLifecycleService {

    private static final Logger LOG = LoggerFactory.getLogger(SalObjectLifecycleServiceImpl.class);

    private final CreateObj createObj;
    private final DeleteObj deleteObj;

    public SalObjectLifecycleServiceImpl(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        createObj = new CreateObj(requestContextStack, deviceContext);
        deleteObj = new DeleteObj(requestContextStack, deviceContext);
    }

    @Override
    public Future<RpcResult<CreateObjOutput>> createObj(final CreateObjInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput>> future = createObj.handleServiceCall(input);
        final SettableFuture<RpcResult<CreateObjOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput output = result.getResult();
                CreateObjOutputBuilder builder = new CreateObjOutputBuilder();
                builder.setObjId(output.getObjId());                
                builder.setParam(output.getParam());
                builder.setGlobResult(output.getGlobResult());
                RpcResultBuilder<CreateObjOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<CreateObjOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

    @Override
    public Future<RpcResult<DeleteObjOutput>> deleteObj(final DeleteObjInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput>> future = deleteObj.handleServiceCall(input);
        final SettableFuture<RpcResult<DeleteObjOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput output = result.getResult();
                DeleteObjOutputBuilder builder = new DeleteObjOutputBuilder();
                builder.setResult(output.getResult());
                RpcResultBuilder<DeleteObjOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<DeleteObjOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

}
