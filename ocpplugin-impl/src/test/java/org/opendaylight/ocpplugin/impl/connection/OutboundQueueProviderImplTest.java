/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.impl.connection;

import junit.framework.TestCase;
import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OutboundQueueProviderImplTest extends TestCase {

    private static final Long DUMMY_ENTRY_NUMBER = 44L;

    private final OutboundQueueProviderImpl outboundQueueProvider = new OutboundQueueProviderImpl();

    @Test
    public void testReserveEntry() throws Exception {

        outboundQueueProvider.onConnectionQueueChanged(null);
        Long returnValue = outboundQueueProvider.reserveEntry();
        assertEquals(null, returnValue);

        OutboundQueue mockedQueue = mock(OutboundQueue.class);
        when(mockedQueue.reserveEntry()).thenReturn(DUMMY_ENTRY_NUMBER);
        outboundQueueProvider.onConnectionQueueChanged(mockedQueue);
        returnValue = outboundQueueProvider.reserveEntry();
        assertEquals(DUMMY_ENTRY_NUMBER, returnValue);
    }

}
