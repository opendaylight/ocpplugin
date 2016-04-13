/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.config.ocp.plugin.impl.rev150811;

import org.opendaylight.ocpplugin.api.ocp.OcpPluginProvider;
import org.opendaylight.ocpplugin.impl.OcpPluginProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Richard Chien <richard.chien@foxconn.com>
 *
 */
public class OcpProviderModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.config.ocp.plugin.impl.rev150811.AbstractOcpProviderModule {

    private static final Logger LOG = LoggerFactory.getLogger(OcpProviderModule.class);

    public OcpProviderModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public OcpProviderModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.config.ocp.plugin.impl.rev150811.OcpProviderModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        LOG.info("Initializing OCP southbound.");
        OcpPluginProvider ocpPluginProvider = new OcpPluginProviderImpl(getOcpVersion(), getRpcRequestsQuota(), 
                                                                        getGlobalNotificationQuota());

        ocpPluginProvider.setRadioHeadConnectionProviders(getOcpRadioheadConnectionProviderDependency());
        ocpPluginProvider.setDataBroker(getDataBrokerDependency());
        ocpPluginProvider.setRpcProviderRegistry(getRpcRegistryDependency());
        ocpPluginProvider.setNotificationProviderService(getNotificationAdapterDependency());
        ocpPluginProvider.setNotificationPublishService(getNotificationPublishAdapterDependency());
        ocpPluginProvider.initialize();

        return ocpPluginProvider;
    }

}
