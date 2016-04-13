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
import com.google.common.util.concurrent.FutureCallback;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContext;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
abstract class AbstractRequestCallback<T> implements FutureCallback<OcpHeader> {
    private final RequestContext<T> context;
    private final Class<?> requestType;

    protected AbstractRequestCallback(final RequestContext<T> context, final Class<?> requestType) {
        this.context = Preconditions.checkNotNull(context);
        this.requestType = Preconditions.checkNotNull(requestType);
    }

    protected final void setResult(@Nullable final RpcResult<T> result) {
        context.setResult(result);
        context.close();
    }

    @Override
    public final void onFailure(final Throwable t) {
        final RpcResultBuilder<T> builder;
        builder = RpcResultBuilder.<T>failed().withError(RpcError.ErrorType.APPLICATION, t.getMessage(), t);
        context.setResult(builder.build());
        RequestContextUtil.closeRequstContext(context);
    }
}
