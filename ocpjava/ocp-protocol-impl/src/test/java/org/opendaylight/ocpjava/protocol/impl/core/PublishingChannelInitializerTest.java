/*
 * Copyright (c) 2014 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javax.net.ssl.SSLEngine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfiguration;
import org.opendaylight.ocpjava.protocol.api.connection.TlsConfigurationImpl;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionAdapterFactory;
import org.opendaylight.ocpjava.protocol.impl.core.connection.ConnectionFacade;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializationFactory;
import org.opendaylight.ocpjava.protocol.impl.serialization.SerializationFactory;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.KeystoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.rev150811.PathType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.radiohead.connection.provider.impl.rev150811.Tls;

/**
 *
 * @author james.hall
 */
public class PublishingChannelInitializerTest {

    @Mock SocketChannel mockSocketCh ;
    @Mock ChannelPipeline mockChPipeline ;
    @Mock RadioHeadConnectionHandler mockSwConnHandler ;
    @Mock ConnectionAdapterFactory mockConnAdaptorFactory;
    @Mock DefaultChannelGroup mockChGrp ;
    @Mock ConnectionFacade mockConnFacade ;
    @Mock Tls mockTls ;
    SSLEngine sslEngine ;

    @Mock SerializationFactory mockSerializationFactory ;
    @Mock DeserializationFactory mockDeserializationFactory ;

    TlsConfiguration tlsConfiguration ;
    InetSocketAddress inetSockAddr;
    TcpChannelInitializer pubChInitializer  ;

    /**
     * Sets up test environment
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pubChInitializer= new TcpChannelInitializer(mockChGrp, mockConnAdaptorFactory) ;
        pubChInitializer.setSerializationFactory(mockSerializationFactory);
        pubChInitializer.setDeserializationFactory(mockDeserializationFactory);
        pubChInitializer.setRadioHeadIdleTimeout(1) ;
        pubChInitializer.getConnectionIterator() ;

        when( mockChGrp.size()).thenReturn(1) ;
        pubChInitializer.setRadioHeadConnectionHandler( mockSwConnHandler ) ;

        inetSockAddr = new InetSocketAddress(InetAddress.getLocalHost(), 8675 ) ;

        when(mockConnAdaptorFactory.createConnectionFacade(mockSocketCh, null))
        .thenReturn(mockConnFacade);
        when(mockSocketCh.remoteAddress()).thenReturn(inetSockAddr) ;
        when(mockSocketCh.localAddress()).thenReturn(inetSockAddr) ;
        when(mockSocketCh.remoteAddress()).thenReturn(inetSockAddr) ;
        when(mockSwConnHandler.accept(eq(InetAddress.getLocalHost()))).thenReturn(true) ;
        when(mockSocketCh.pipeline()).thenReturn(mockChPipeline) ;

        tlsConfiguration = new TlsConfigurationImpl(KeystoreType.JKS, "/selfSignedRadioHead", PathType.CLASSPATH,
                KeystoreType.JKS, "/selfSignedController", PathType.CLASSPATH);
    }


    /**
     * Test channel initialization with encryption config set
     */
    @Test
    public void testinitChannelEncryptionSet()  {
        pubChInitializer.setTlsConfiguration(tlsConfiguration);
        pubChInitializer.initChannel(mockSocketCh) ;

        verifyCommonHandlers();
        verify(mockChPipeline, times(1)).addLast(eq(PipelineHandlers.SSL_HANDLER.name()),any(SslHandler.class)) ;
    }

    /**
     * Test channel initialization with null encryption config
     */
    @Test
    public void testinitChannelEncryptionSetNullTls()  {
        pubChInitializer.setTlsConfiguration(null);
        pubChInitializer.initChannel(mockSocketCh) ;

        verifyCommonHandlers();
        verify(mockChPipeline, times(0)).addLast(eq(PipelineHandlers.SSL_HANDLER.name()),any(SslHandler.class)) ;
    }

    /**
     * Test channel initialization without setting the encryption
     */
    @Test
    public void testinitChannelEncryptionNotSet()  {
        // Without encryption, only the common
        pubChInitializer.initChannel(mockSocketCh) ;

        verifyCommonHandlers();
    }

    /**
     * Test disconnect on new connection rejected
     * @throws UnknownHostException
     */
    @Test
    public void testinitChannelNoEncryptionAcceptFails() throws UnknownHostException  {
        when(mockSwConnHandler.accept(eq(InetAddress.getLocalHost()))).thenReturn(false) ;
        pubChInitializer.initChannel(mockSocketCh) ;

        verify(mockSocketCh, times(1)).disconnect();
        verify(mockChPipeline, times(0))
        .addLast( any(String.class), any(ChannelHandler.class) ) ;
    }

    /**
     * Test channel close on exception during initialization
     */
    @Test
    public void testExceptionThrown() {
        doThrow(new IllegalArgumentException()).when(mockSocketCh).pipeline() ;
        pubChInitializer.initChannel(mockSocketCh);

        verify( mockSocketCh, times(1)).close() ;
    }

    /**
     * All paths should install these six handlers:
     */
    private void verifyCommonHandlers() {
        verify(mockChPipeline, times(1)).addLast(eq(PipelineHandlers.IDLE_HANDLER.name()),any(IdleHandler.class)) ;
        verify(mockChPipeline, times(1)).addLast(eq(PipelineHandlers.OCP_XML_DECODER.name()),any(OCPXmlDecoder.class)) ;
        verify(mockChPipeline, times(1)).addLast(eq(PipelineHandlers.OCP_DECODER.name()),any(OCPDecoder.class)) ;
        verify(mockChPipeline, times(1)).addLast(eq(PipelineHandlers.OCP_ENCODER.name()),any(OCPEncoder.class)) ;
        verify(mockChPipeline, times(1)).addLast(eq(PipelineHandlers.DELEGATING_INBOUND_HANDLER.name()),any(DelegatingInboundHandler.class));
        assertEquals(1, pubChInitializer.size()) ;
    }
}
