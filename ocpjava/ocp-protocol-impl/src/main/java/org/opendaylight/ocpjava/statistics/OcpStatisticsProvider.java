/*
 * Copyright (c) 2017 Inocybe Technologies and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpjava.statistics;

import org.opendaylight.controller.md.sal.common.util.jmx.AbstractMXBean;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.statistics.config.rev170807.OcpStatisticsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instantiates the OCP statistics components.
 *
 * @author Thomas Pantelis
 */
public class OcpStatisticsProvider implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(OcpStatisticsProvider.class);

    private final OcpStatisticsConfig statsConfig;

    private final OcpStatisticsCollectionRuntimeMXBeanImpl collectionBean =
            new OcpStatisticsCollectionRuntimeMXBeanImpl();

    public OcpStatisticsProvider(OcpStatisticsConfig statsConfig) {
        this.statsConfig = statsConfig;
    }

    public void init() {
        OcpStatisticsCounters.getInstance().startCounting(statsConfig.isOcpStatisticsCollect(),
                statsConfig.getLogReportDelay());

        collectionBean.register();

        LOG.info("OcpStatisticsCollection service initialized");
    }

    @Override
    public void close() {
        OcpStatisticsCounters.getInstance().stopCounting();
        collectionBean.unregister();

        LOG.info("OcpStatisticsCollection service closed");
    }

    private static class OcpStatisticsCollectionRuntimeMXBeanImpl extends AbstractMXBean
            implements OcpStatisticsCollectionRuntimeMXBean {
        OcpStatisticsCollectionRuntimeMXBeanImpl() {
            super("ocp-statistics-collection-service-impl", "RuntimeBean", "ocp-statistics-collection-service-impl");
        }

        @Override
        public String printOcpjavaOcpStatistics() {
            return OcpStatisticsCounters.getInstance().printOcpStatistics();
        }

        @Override
        public String getMsgOcpStatistics() {
            return printOcpjavaOcpStatistics();
        }

        @Override
        public String resetOcpjavaOcpStatistics() {
            OcpStatisticsCounters.getInstance().resetCounters();
            return "OcpStatistics have been reset";
        }
    }
}
