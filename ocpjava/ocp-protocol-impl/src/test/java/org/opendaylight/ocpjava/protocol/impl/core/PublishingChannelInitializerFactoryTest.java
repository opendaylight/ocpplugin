/*
 * Copyright (c) 2014 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfigurationImpl;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializationFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializationFactory;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.KeystoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.PathType;

/**
 *
 * @author jameshall
 */
public class PublishingChannelInitializerFactoryTest {

    TlsConfiguration tlsConfiguration ;
    ChannelInitializerFactory factory;
    private final long radioHeadIdleTimeOut = 60;
    @Mock RadioHeadConnectionHandler radioHeadConnectionHandler ;
    @Mock SerializationFactory serializationFactory;
    @Mock DeserializationFactory deserializationFactory ;

    /**
     * Sets up test environment
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        factory = new ChannelInitializerFactory();
        tlsConfiguration = new TlsConfigurationImpl(KeystoreType.JKS, "/exemplary-ctlTrustStore",
                PathType.CLASSPATH, KeystoreType.JKS, "/exemplary-ctlKeystore", PathType.CLASSPATH);
        factory.setDeserializationFactory(deserializationFactory);
        factory.setSerializationFactory(serializationFactory);
        factory.setRadioHeadConnectionHandler(radioHeadConnectionHandler);
        factory.setRadioHeadIdleTimeout(radioHeadIdleTimeOut);
        factory.setTlsConfig(tlsConfiguration);
    }

    /**
     * Test {@link TcpChannelInitializer} creation
     */
    @Test
    public void testCreatePublishingChannelInitializer() {
        TcpChannelInitializer initializer = factory.createPublishingChannelInitializer() ;
        assertNotNull( initializer );
    }
}