/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.api;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;

/**
 * OCP related constants
 */
public final class OcpConstants {

    private OcpConstants() {
        throw new UnsupportedOperationException("OCP plugin Constants holder class");
    }

    public static final String OCP_URI_PREFIX = "ocp:";
    public static final long MAX_RPC_REPLY_TIMEOUT = 5000;
    public static final XsdUnsignedShort DEFAULT_TLM_TIMEOUT = new XsdUnsignedShort(50);

    /** Supported OCP version */
    public static String OCP_VERSION = "unknown";
}
