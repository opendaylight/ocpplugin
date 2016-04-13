/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp;

import java.util.Collection;
import org.opendaylight.controller.md.sal.binding.api.BindingService;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.ocpjava.protocol.spi.connection.RadioHeadConnectionProvider;

public interface OcpPluginProvider extends AutoCloseable, BindingService {

    /**
     * Method sets ocp java's connection providers.
     */
    void setRadioHeadConnectionProviders(Collection<RadioHeadConnectionProvider> radioHeadConnectionProvider);

    /**
     * setter
     *
     * @param dataBroker
     */
    void setDataBroker(DataBroker dataBroker);

    void setRpcProviderRegistry(RpcProviderRegistry rpcProviderRegistry);

    void setNotificationProviderService(NotificationService notificationProviderService);

    void setNotificationPublishService(NotificationPublishService notificationPublishService);

    /**
     * Method initializes all DeviceManager, RpcManager and related contexts.
     */
    void initialize();

    }
