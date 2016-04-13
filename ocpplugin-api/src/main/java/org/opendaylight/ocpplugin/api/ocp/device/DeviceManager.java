/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api.ocp.device;

import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceConnectedHandler;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceContextClosedHandler;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceInitializationPhaseHandler;
import org.opendaylight.ocpplugin.api.ocp.device.handlers.DeviceInitializator;

/*
 * This interface is responsible for instantiating DeviceContext and
 * registering transaction chain for each DeviceContext. Each device
 * has its own device context managed by this manager.
 */
public interface DeviceManager extends DeviceConnectedHandler,
        DeviceInitializator,
        DeviceInitializationPhaseHandler, DeviceContextClosedHandler {

    /**
     * Sets notification receiving service
     *
     * @param notificationService
     */
    void setNotificationService(NotificationService notificationService);

    /**
     * Sets notification publish service
     *
     * @param notificationPublishService
     */
    void setNotificationPublishService(NotificationPublishService notificationPublishService);

    /**
     * invoked after all services injected
     */
    void initialize();
}

