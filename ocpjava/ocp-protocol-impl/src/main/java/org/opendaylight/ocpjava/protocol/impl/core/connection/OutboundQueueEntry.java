/*
 * Copyright (c) 2015 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueueException;
///import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev130731.BarrierInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;

final class OutboundQueueEntry {
    private static final Logger LOG = LoggerFactory.getLogger(OutboundQueueEntry.class);
    private FutureCallback<OcpHeader> callback;
    private OcpHeader message;
    private boolean completed;
    private volatile boolean committed;

    void commit(final OcpHeader message, final FutureCallback<OcpHeader> callback) {
        this.message = message;
        this.callback = callback;

        // Volatile write, needs to be last
        committed = true;
    }

    void reset() {
        callback = null;
        completed = false;
        message = null;

        // Volatile write, needs to be last
        committed = false;
    }

    boolean isCommitted() {
        return committed;
    }

    boolean isCompleted() {
        return completed;
    }

    OcpHeader takeMessage() {
        final OcpHeader ret = message;
        message = null;
        return ret;
    }

    boolean complete(final OcpHeader response) {
        Preconditions.checkState(!completed, "Attempted to complete a completed message with response %s", response);

        // Multipart requests are special, we have to look at them to see
        // if there is something outstanding and adjust ourselves accordingly
        final boolean reallyComplete = true;
        completed = reallyComplete;

        if (callback != null) {
            callback.onSuccess(response);
            if (reallyComplete) {
                // We will not need the callback anymore, make sure it can be GC'd
                callback = null;
            }
        }
        LOG.debug("Entry {} completed {} with response {}", this, completed, response);
        return reallyComplete;
    }

    void fail(final OutboundQueueException cause) {
        if (!completed) {
            completed = true;
            if (callback != null) {
                callback.onFailure(cause);
                callback = null;
            }
        } else {
            LOG.warn("Ignoring failure {} for completed message", cause);
        }
    }
}
