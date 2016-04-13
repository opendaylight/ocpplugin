/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */


package org.opendaylight.ocpjava.protocol.spi.connection;

import org.opendaylight.ocpjava.protocol.api.connection.ConnectionConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;


import com.google.common.util.concurrent.ListenableFuture;

/**
 * @author mirehak
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public interface RadioHeadConnectionProvider extends AutoCloseable {

    /**
     * @param configuration [protocol, port, address and supported features]
     */
    void setConfiguration(ConnectionConfiguration configuration);

    /**
     * start listening to radioHeads, but please don't forget to do
     * {@link #setRadioHeadConnectionHandler(RadioHeadConnectionHandler)} first
     * @return future, triggered to true, when listening channel is up and running
     */
    ListenableFuture<Boolean> startup();

    /**
     * stop listening to radioHeads
     * @return future, triggered to true, when all listening channels are down
     */
    ListenableFuture<Boolean> shutdown();

    /**
     * @param radioHeadConHandler instance being informed when new radioHead connects
     */
    void setRadioHeadConnectionHandler(RadioHeadConnectionHandler radioHeadConHandler);
}
