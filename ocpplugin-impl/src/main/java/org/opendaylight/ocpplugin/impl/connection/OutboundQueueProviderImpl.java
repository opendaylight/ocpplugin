/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection;

import com.google.common.util.concurrent.FutureCallback;
import javax.annotation.Nonnull;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueue;
import org.opendaylight.ocpplugin.api.ocp.connection.OutboundQueueProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutboundQueueProviderImpl implements OutboundQueueProvider {
    private static final Logger LOG = LoggerFactory.getLogger(OutboundQueueProviderImpl.class);
    private volatile OutboundQueue outboundQueue;

    @Override
    public synchronized void onConnectionQueueChanged(final OutboundQueue queue) {
        LOG.debug("Replacing queue {} with {}", outboundQueue, queue);
        outboundQueue = queue;
        notifyAll();
    }

    @Override
    public Long reserveEntry() {
        for (;;) {
            OutboundQueue queue = outboundQueue;
            if (queue == null) {
                LOG.debug("No queue present, failing request");
                return null;
            }

            final Long ret = queue.reserveEntry();
            if (ret != null) {
                return ret;
            }

            LOG.debug("Reservation failed, trying to recover");
            synchronized (this) {
                while (queue.equals(outboundQueue)) {
                    LOG.debug("Queue {} is not replaced yet, going to sleep", queue);
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        LOG.info("Interrupted while waiting for entry", e);
                        return null;
                    }
                }
            }
        }
    }

    @Override
    public void commitEntry(final Long xid, final OcpHeader message, final FutureCallback<OcpHeader> callback) {
        outboundQueue.commitEntry(xid, message, callback);
    }
}
