/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.ocp.it;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.opendaylight.ocpjava.protocol.impl.clients.SimpleClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public abstract class SimulatorAssistant {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(SimulatorAssistant.class);

    /**
     * @param radioHeadSim
     * @throws InterruptedException
     */
    public static void waitForRadioHeadSimulatorOn(SimpleClient radioHeadSim) {
        try {
            radioHeadSim.getIsOnlineFuture().get(6, TimeUnit.SECONDS); // intentionally ignoring future inner value
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            LOG.error("failed to start radio head simulator: {}", e.getMessage(), e);
            Assert.fail("failed to start radio head simulator");
        }
    }

    /**
     * @param radioHeadSim 
     * @param scenarioPool
     * @param failsafeTimeout 
     */
    public static void tearDownRadioHeadSimulatorAfterScenario(SimpleClient radioHeadSim, ThreadPoolExecutor scenarioPool, long failsafeTimeout) {
        try {
            LOG.debug("tearing down simulator");
            radioHeadSim.getScenarioDone().get(failsafeTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            String msg = "waiting for scenario to finish failed: "+e.getMessage();
            LOG.error(msg, e);
            //FIXME: Enable the assert.
            //Assert.fail(msg);
        } finally {
            scenarioPool.shutdownNow();
            scenarioPool.purge();
        }
    
        try {
            LOG.debug("checking if simulator succeeded to connect to controller");
            boolean simulatorWasOnline = radioHeadSim.getIsOnlineFuture().get(100, TimeUnit.MILLISECONDS);
            Assert.assertTrue("simulator failed to connect to controller", simulatorWasOnline);
        } catch (Exception e) {
            String message = "simulator probably failed to connect to controller";
            LOG.error(message, e);
            Assert.fail(message);
        }
    }

}
