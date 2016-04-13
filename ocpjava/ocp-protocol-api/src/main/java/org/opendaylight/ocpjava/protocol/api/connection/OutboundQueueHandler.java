/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.connection;

import com.google.common.annotations.Beta;
import javax.annotation.Nonnull;

/**
 * Handler of the outbound queue. The queue has a maximum depth assigned when the
 * handler is registered.
 */
@Beta
public interface OutboundQueueHandler {
    /**
     * Invoked whenever the underlying queue is refreshed. Implementations should
     * ensure they are talking to the latest queue
     * @param queue New queue instance, null indicates a shutdown, e.g. the queue
     *              is no longer available.
     */
    void onConnectionQueueChanged(OutboundQueue queue);
}
