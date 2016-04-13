/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation 
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.services;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.math.BigInteger;
import javax.annotation.Nonnull;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueue;
import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContext;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContextStack;
import org.opendaylight.ocpplugin.api.ocp.device.Xid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yangtools.yang.binding.DataContainer;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
abstract class AbstractService<I, O> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);

    private final RequestContextStack requestContextStack;
    private final DeviceContext deviceContext;

    public AbstractService(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        this.requestContextStack = requestContextStack;
        this.deviceContext = deviceContext;
    }

    public RequestContextStack getRequestContextStack() {
        return requestContextStack;
    }

    public DeviceContext getDeviceContext() {
        return deviceContext;
    }

    protected abstract OcpHeader buildRequest(Xid xid, I input);

    protected abstract FutureCallback<OcpHeader> createCallback(RequestContext<O> context, Class<?> requestType);

    public final ListenableFuture<RpcResult<O>> handleServiceCall(@Nonnull final I input) {
        Preconditions.checkNotNull(input);

        final Class<?> requestType;
        if (input instanceof DataContainer) {
            requestType = ((DataContainer) input).getImplementedInterface();
        } else {
            requestType = input.getClass();
        }

        LOG.trace("Handling general service call");
        final RequestContext<O> requestContext = requestContextStack.createRequestContext();
        if (requestContext == null) {
            LOG.trace("Request context refused.");
            return failedFuture();
        }

        if (requestContext.getXid() == null) {
            return RequestContextUtil.closeRequestContextWithRpcError(requestContext, "Outbound queue wasn't able to reserve XID.");
        }

        final Xid xid = requestContext.getXid();
        OcpHeader request = null;
        try {
            request = buildRequest(xid, input);
            Verify.verify(xid.getValue().equals(request.getXid()), "Expected XID %s got %s", xid.getValue(), request.getXid());
        } catch (Exception e) {
            LOG.error("Failed to build request for {}, forfeiting request {}", input, xid.getValue(), e);
            // FIXME: complete the requestContext
        } finally {
            final OutboundQueue outboundQueue = getDeviceContext().getConnectionContext().getOutboundQueueProvider();
            outboundQueue.commitEntry(xid.getValue(), request, createCallback(requestContext, requestType));
        }

        return requestContext.getFuture();
    }

    protected static <T> ListenableFuture<RpcResult<T>> failedFuture() {
        final RpcResult<T> rpcResult = RpcResultBuilder.<T>failed()
                .withError(RpcError.ErrorType.APPLICATION, "", "Request quota exceeded").build();
        return Futures.immediateFuture(rpcResult);
    }
}
