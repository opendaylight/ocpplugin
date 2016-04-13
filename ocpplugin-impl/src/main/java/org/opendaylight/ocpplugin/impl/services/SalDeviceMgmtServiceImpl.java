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
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SalDeviceMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.ReResetInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.ReResetOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.ReResetOutputBuilder;

import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class SalDeviceMgmtServiceImpl implements SalDeviceMgmtService {

    private static final Logger LOG = LoggerFactory.getLogger(SalDeviceMgmtServiceImpl.class);
    
    private final HealthCheck healthCheck;
    private final SetTime setTime;
    private final ReReset reReset;

    public SalDeviceMgmtServiceImpl(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        healthCheck = new HealthCheck(requestContextStack, deviceContext);
        setTime = new SetTime(requestContextStack, deviceContext);
        reReset = new ReReset(requestContextStack, deviceContext);
    }

    @Override
    public Future<RpcResult<HealthCheckOutput>> healthCheck(final HealthCheckInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput>> future = healthCheck.handleServiceCall(input);
        final SettableFuture<RpcResult<HealthCheckOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput output = result.getResult();
                HealthCheckOutputBuilder builder = new HealthCheckOutputBuilder(); 
                builder.setResult(output.getResult());                  
                RpcResultBuilder<HealthCheckOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<HealthCheckOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

    @Override
    public Future<RpcResult<SetTimeOutput>> setTime(final SetTimeInput input)  {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput>> future = setTime.handleServiceCall(input);
        final SettableFuture<RpcResult<SetTimeOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput output = result.getResult();
                SetTimeOutputBuilder builder = new SetTimeOutputBuilder();
                builder.setResult(output.getResult());
                RpcResultBuilder<SetTimeOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<SetTimeOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

    @Override
    public Future<RpcResult<ReResetOutput>> reReset(final ReResetInput input) {
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput>> future = reReset.handleServiceCall(input);
        final SettableFuture<RpcResult<ReResetOutput>> finalFuture = SettableFuture.create();
        Futures.addCallback(future, new FutureCallback<RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput>>() {
            @Override
            public void onSuccess(final RpcResult<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput> result) {
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput output = result.getResult();
                ReResetOutputBuilder builder = new ReResetOutputBuilder();
                builder.setResult(output.getResult());
                RpcResultBuilder<ReResetOutput> rpcResultBuilder = RpcResultBuilder.success(builder);
                finalFuture.set(rpcResultBuilder.build());
            }

            @Override
            public void onFailure(final Throwable t) {
                RpcResultBuilder<ReResetOutput> rpcResultBuilder = RpcResultBuilder.failed();
                finalFuture.set(rpcResultBuilder.build());
            }
        });

        return finalFuture;
    }

}
