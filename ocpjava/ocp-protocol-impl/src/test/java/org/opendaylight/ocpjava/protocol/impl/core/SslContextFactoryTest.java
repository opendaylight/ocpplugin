/*
 * Copyright (c) 2014 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import static org.junit.Assert.assertNotNull;

import javax.net.ssl.SSLContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfigurationImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.KeystoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.PathType;

/**
 *
 * @author jameshall
 */
public class SslContextFactoryTest {

    SslContextFactory sslContextFactory;
    TlsConfiguration tlsConfiguration ;

    /**
     * Sets up test environment
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tlsConfiguration = new TlsConfigurationImpl(KeystoreType.JKS, "/exemplary-ctlTrustStore",
                PathType.CLASSPATH, KeystoreType.JKS, "/exemplary-ctlKeystore", PathType.CLASSPATH) ;
        sslContextFactory = new SslContextFactory(tlsConfiguration);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetServerContext() throws Exception {
        SSLContext context  = sslContextFactory.getServerContext() ;

        assertNotNull( context );
    }

}

