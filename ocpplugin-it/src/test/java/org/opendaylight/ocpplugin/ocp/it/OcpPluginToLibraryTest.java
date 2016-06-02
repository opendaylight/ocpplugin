/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation 
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.ocp.it;

import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.util.Deque;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendaylight.ocpjava.protocol.impl.clients.ClientEvent;
import org.opendaylight.ocpjava.protocol.impl.clients.ScenarioHandler;
import org.opendaylight.ocpjava.protocol.impl.clients.SimpleClient;
import org.opendaylight.ocpjava.protocol.impl.clients.SleepEvent;
import org.opendaylight.ocpplugin.impl.common.ThreadPoolLoggingExecutor;
import org.opendaylight.ocpplugin.api.ocp.OcpPluginProvider;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * covers basic handshake scenarios
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class OcpPluginToLibraryTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(OcpPluginToLibraryTest.class);

    private final ArrayBlockingQueue<Runnable> SCENARIO_POOL_QUEUE = new ArrayBlockingQueue<>(1);

    @Inject @Filter(timeout=60000)
    OcpPluginProvider ocpPluginProvider;

    @Inject @Filter(timeout=60000)
    BundleContext ctx;

    private SimpleClient radioHeadSim;
    private ThreadPoolLoggingExecutor scenarioPool;

    /**
     * test setup
     * @throws InterruptedException
     */
    @Before
    public void setUp() throws InterruptedException {
        LOG.debug("ocpPluginProvider: "+ocpPluginProvider);
        radioHeadSim = createSimpleClient();
        scenarioPool = new ThreadPoolLoggingExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, SCENARIO_POOL_QUEUE, "scenario");
        Thread.sleep(5000L);
    }

    /**
     * test tear down
     */
    @After
    public void tearDown() {
        SimulatorAssistant.waitForRadioHeadSimulatorOn(radioHeadSim);
        SimulatorAssistant.tearDownRadioHeadSimulatorAfterScenario(radioHeadSim, scenarioPool, getFailSafeTimeout());
    }

    /**
     * test basic integration with OCPLib running the handshake
     * @throws Exception
     */
    @Test
    public void handshake() throws Exception {
        LOG.debug("handshake integration test");

        radioHeadSim.setSecuredClient(false);
        Deque<ClientEvent> handshakeScenario = ScenarioFactory.createHandshakeScenario(
                                               "4.1.1", "TST", "1", false);

        ScenarioHandler scenario = new ScenarioHandler(handshakeScenario);
        radioHeadSim.setScenarioHandler(scenario);
        scenarioPool.execute(radioHeadSim);
    }

    /**
     * @return
     */
    private static SimpleClient createSimpleClient() {
        return new SimpleClient("localhost", 1033);
    }

    /**
     * @return timeout for case of failure
     */
    private static long getFailSafeTimeout() {
        return 20000;
    }


    /**
     * @return bundle options
     */
    @Configuration
    public Option[] config() {
        LOG.info("configuring...");
        return options(
                systemProperty("osgi.console").value("2401"),
                systemProperty("osgi.bundles.defaultStartLevel").value("4"),
                systemProperty("pax.exam.osgi.unresolved.fail").value("true"),
                OcpPaxOptionsAssistant.osgiConsoleBundles(),
                OcpPaxOptionsAssistant.loggingBudles(),
                OcpPaxOptionsAssistant.ocpPluginBundles());
    }

}

