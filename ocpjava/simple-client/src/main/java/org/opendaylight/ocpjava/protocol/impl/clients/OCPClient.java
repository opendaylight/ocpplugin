/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.clients;

import com.google.common.util.concurrent.SettableFuture;

/**
 * Unifying interface for simple clients / radioHead simulators
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 */
public interface OCPClient extends Runnable {

    /**
     * @return the isOnlineFuture which is set when client is started
     */
    SettableFuture<Boolean> getIsOnlineFuture();

    /**
     * @return the scenarioDone when scenario is successfully finished
     */
    SettableFuture<Boolean> getScenarioDone();

    /**
     * @param scenario list of desired actions
     */
    void setScenarioHandler(ScenarioHandler scenario);

    /**
     * @param securedClient true is client should use encrypted communication,
     * false otherwise
     */
    void setSecuredClient(boolean securedClient);
}
