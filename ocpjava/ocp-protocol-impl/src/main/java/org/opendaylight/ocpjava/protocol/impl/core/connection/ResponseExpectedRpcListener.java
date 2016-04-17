/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import java.util.concurrent.TimeoutException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;

final class ResponseExpectedRpcListener<T extends OcpHeader> extends AbstractRpcListener<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseExpectedRpcListener.class);
    private final Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> cache;
    private final RpcResponseKey key;

    ResponseExpectedRpcListener(final Object message, final String failureInfo,
            final Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> cache, final RpcResponseKey key) {
        super(message, failureInfo);
        this.cache = Preconditions.checkNotNull(cache);
        this.key = Preconditions.checkNotNull(key);
    }

    public void discard() {
        LOG.warn("Request for {} did not receive a response", key);
        failedRpc(new TimeoutException("Request timed out"));
    }

    @SuppressWarnings("unchecked")
    public void completed(final OcpHeader message) {
        successfulRpc((T)message);
    }

    @SuppressWarnings("unchecked")
    public void ocpcompleted(final OcpHeader message) {
        successfulRpc((T)message);
    }

    @Override
    protected void operationSuccessful() {
        LOG.debug("Request for {} sent successfully", key);
        cache.put(key, this);
    }
}
