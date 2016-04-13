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
import org.opendaylight.ocpplugin.api.ocp.device.DeviceContext;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContext;
import org.opendaylight.ocpplugin.api.ocp.device.RequestContextStack;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.opendaylight.yangtools.yang.binding.DataObject;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public abstract class AbstractSimpleService<I, O extends DataObject> extends AbstractService<I, O> {
    private final Class<O> clazz;

    protected AbstractSimpleService(final RequestContextStack requestContextStack, final DeviceContext deviceContext, final Class<O> clazz) {
        super(requestContextStack, deviceContext);
        this.clazz = Preconditions.checkNotNull(clazz);
    }

    @Override
    protected final FutureCallback<OcpHeader> createCallback(final RequestContext<O> context, final Class<?> requestType) {
        return SimpleRequestCallback.create(context, requestType, clazz);
    }
}
