/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.ocpplugin.applications.ocpservice.OcpService;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataBroker.DataChangeScope;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.OcpServiceService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.ResourceModel;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.AbstractOcpServiceModule;
import org.opendaylight.controller.sal.binding.api.data.DataProviderService;
import org.opendaylight.yangtools.concepts.ObjectRegistration;
import org.opendaylight.controller.md.sal.common.api.RegistrationListener;
import org.opendaylight.controller.md.sal.common.api.data.AsyncConfigurationCommitCoordinator;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.opendaylight.controller.sal.core.api.BrokerService;
import org.opendaylight.controller.config.api.DependencyResolver;
import org.opendaylight.controller.config.api.ModuleIdentifier;
import org.opendaylight.controller.sal.core.api.Broker;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/*
 * @author Jason Yuan <jason.cw.yuan@foxconn.com>
 *
 */
public class OcpServiceModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.AbstractOcpServiceModule {
    private static final Logger LOG = LoggerFactory.getLogger(OcpServiceModule.class); 
    public OcpServiceModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public OcpServiceModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.OcpServiceModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        LOG.info("OcpServiceModule Instance Created");
        final OcpService ocpService = new OcpService(getRpcRegistryDependency());
        ocpService.setNotificationProvider(getNotificationServiceDependency());
        ocpService.setDataBroker(getDataBrokerDependency());

        final ListenerRegistration<NotificationListener> ocpListenerReg =
               getNotificationServiceDependency().registerNotificationListener(ocpService);              
        final BindingAwareBroker.RpcRegistration<OcpServiceService> rpcRegistration = getRpcRegistryDependency()
                .addRpcImplementation(OcpServiceService.class, ocpService);

        final class AutoCloseableToaster implements AutoCloseable {

            @Override
            public void close() throws Exception {
                rpcRegistration.close();
                ocpListenerReg.close();
                closeQuietly(ocpService);
                LOG.info("OcpServiceModule (instance {}) torn down.", this);
            }

            private void closeQuietly(final AutoCloseable resource) {
                try {
                    resource.close();
                } catch (final Exception e) {
                    LOG.debug("Ignoring exception while closing {}", resource, e);
                }
            }
        }

        AutoCloseable ret = new AutoCloseableToaster(); 
        LOG.info("OcpServiceModule initialized.");
        return ret;   
    }

}
