/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.radiohead.connection.provider.impl.rev150811;

import org.opendaylight.ocpjava.protocol.api.connection.OcpStatisticsConfiguration;
import org.opendaylight.ocpjava.protocol.spi.statistics.OcpStatisticsHandler;
import org.opendaylight.ocpjava.statistics.OcpStatisticsCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* This is the definition of statistics collection module identity.
*/
public class OcpStatisticsCollectionModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.radiohead.connection.provider.impl.rev150811.AbstractOcpStatisticsCollectionModule {

    private static final Logger LOG = LoggerFactory.getLogger(OcpStatisticsCollectionModule.class);

    public OcpStatisticsCollectionModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public OcpStatisticsCollectionModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.radiohead.connection.provider.impl.rev150811.OcpStatisticsCollectionModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        final OcpStatistics statistics = getOcpStatistics();
        final OcpStatisticsCounters statsCounter = OcpStatisticsCounters.getInstance();
        OcpStatisticsConfiguration statsConfig = null;
        if (statistics != null) {
            statsConfig = new OcpStatisticsConfiguration() {

                @Override
                public boolean getOcpStatisticsCollect() {
                    if (statistics.getOcpStatisticsCollect() != null) {
                        return statistics.getOcpStatisticsCollect().booleanValue();
                    }
                    return false;
                }

                @Override
                public int getLogReportDelay() {
                    if (statistics.getLogReportDelay() != null) {
                        return statistics.getLogReportDelay().intValue();
                    }
                    return 0;
                }
            };
        }
        if (statsConfig != null) {
            statsCounter.startCounting(statsConfig.getOcpStatisticsCollect(), statsConfig.getLogReportDelay());
        } else {
            LOG.debug("Unable to start OcpStatisticCounter - wrong configuration");
        }

        /* Internal MXBean implementation */
        final OcpStatisticsCollectionRuntimeMXBean collectionBean = new OcpStatisticsCollectionRuntimeMXBean() {

            @Override
            public String printOcpjavaOcpStatistics() {
                if (statsCounter != null) {
                    return statsCounter.printOcpStatistics();
                }
                return "OcpStatistics collection is not avaliable.";
            }
            @Override
            public String getMsgOcpStatistics() {
                return printOcpjavaOcpStatistics();
            }
            @Override
            public String resetOcpjavaOcpStatistics() {
                statsCounter.resetCounters();
                return "OcpStatistics have been reset";
            }
        };

        /* MXBean registration */
        final OcpStatisticsCollectionRuntimeRegistration runtimeReg =
                getRootRuntimeBeanRegistratorWrapper().register(collectionBean);

        /* Internal OcpStatisticsCollectionService implementation */
        final class AutoClosableOcpStatisticsCollection implements OcpStatisticsHandler, AutoCloseable {

            @Override
            public void close() {
                if (runtimeReg != null) {
                    try {
                        runtimeReg.close();
                    }
                    catch (Exception e) {
                        String errMsg = "Error by stoping OcpStatisticsCollectionService.";
                        LOG.error(errMsg, e);
                        throw new IllegalStateException(errMsg, e);
                    }
                }
                LOG.info("OcpStatisticsCollection Service consumer (instance {} turn down.)", this);
            }

            @Override
            public void resetCounters() {
                statsCounter.resetCounters();
            }

            @Override
            public String printOcpStatistics() {
                return statsCounter.printOcpStatistics();
            }
        }

        AutoCloseable ret = new AutoClosableOcpStatisticsCollection();
        LOG.info("OcpStatisticsCollection service (instance {}) initialized.", ret);
        return ret;
    }
}
