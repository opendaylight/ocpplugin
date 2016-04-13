/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.connection;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public interface ThreadConfiguration {

    /**
     * @return desired number of workerThreads processing the OCP I/O
     */
    int getWorkerThreadCount();

    /**
     * @return desired number of bossThreads registering incomming OCP connections
     */
    int getBossThreadCount();
}
