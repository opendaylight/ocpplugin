/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.clients;

/**
 * Uniting interface used for scenario support
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public interface ClientEvent {

    /**
     * Common method for triggering events
     * @return true if event executed successfully
     */
    boolean eventExecuted();
}
