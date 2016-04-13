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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.SalConfigMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutputBuilder;

import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class SalConfigMgmtServiceImpl implements SalConfigMgmtService {

    private static final Logger LOG = LoggerFactory.getLogger(SalConfigMgmtServiceImpl.class);

    private final GetParam getParam;
    private final ModifyParam modifyParam;

    public SalConfigMgmtServiceImpl(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        getParam = new GetParam(requestContextStack, deviceContext);
        modifyParam = new ModifyParam(requestContextStack, deviceContext);
    }

    @Override
    public Future<RpcResult<GetParamOutput>> getParam(final GetParamInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput>> future = getParam.handleServiceCall(input);
        final SettableFuture<RpcResult<GetParamOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput output = result.getResult();
                GetParamOutputBuilder builder = new GetParamOutputBuilder();
                builder.setObj(output.getObj());
                builder.setResult(output.getResult());
                RpcResultBuilder<GetParamOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<GetParamOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

    @Override
    public Future<RpcResult<ModifyParamOutput>> modifyParam(final ModifyParamInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput>> future = modifyParam.handleServiceCall(input);
        final SettableFuture<RpcResult<ModifyParamOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput output = result.getResult();
                ModifyParamOutputBuilder builder = new ModifyParamOutputBuilder();
                builder.setObj(output.getObj());
                builder.setGlobResult(output.getGlobResult());
                RpcResultBuilder<ModifyParamOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<ModifyParamOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

}
