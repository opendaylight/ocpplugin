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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.SalFaultMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutputBuilder;

import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class SalFaultMgmtServiceImpl implements SalFaultMgmtService {

    private static final Logger LOG = LoggerFactory.getLogger(SalFaultMgmtServiceImpl.class);

    private final GetFault getFault;

    public SalFaultMgmtServiceImpl(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        getFault = new GetFault(requestContextStack, deviceContext);
    }

    @Override
    public Future<RpcResult<GetFaultOutput>> getFault(final GetFaultInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput>> future =  getFault.handleServiceCall(input);
        final SettableFuture<RpcResult<GetFaultOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput output = result.getResult();
                GetFaultOutputBuilder builder = new GetFaultOutputBuilder();
                builder.setObj(output.getObj());
                builder.setResult(output.getResult());
                RpcResultBuilder<GetFaultOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<GetFaultOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

}
