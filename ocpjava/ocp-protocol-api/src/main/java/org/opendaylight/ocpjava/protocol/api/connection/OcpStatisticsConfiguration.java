/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.api.connection;

/**
 * Used for OcpStatisticsCounter configuration
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public interface OcpStatisticsConfiguration {

    /**
     * @return true if statistics are / will be collected, false otherwise
     */
    boolean getOcpStatisticsCollect();

    /**
     * @return delay between two statistics logs (in milliseconds)
     */
    int getLogReportDelay();
}
