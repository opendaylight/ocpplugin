/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.it.integration;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.opendaylight.ocpjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionReadyListener;
import org.opendaylight.ocpjava.protocol.api.connection.RadioHeadConnectionHandler;
import org.opendaylight.ocpjava.protocol.impl.core.RadioHeadConnectionProviderImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OriHelloAckRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdDateTime;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.Obj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.ObjBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.Param;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.ParamBuilder;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.OcpProtocolListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChange;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.DisconnectEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.RadioHeadIdleEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.SystemNotificationsListener;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.SettableFuture;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class MockPlugin implements OcpProtocolListener, OcpExtensionListener, RadioHeadConnectionHandler,
        SystemNotificationsListener, ConnectionReadyListener {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MockPlugin.class);
    protected volatile ConnectionAdapter adapter;
    private SettableFuture<Void> finishedFuture;
    private int idleCounter = 0;

    /** Creates MockPlugin */
    public MockPlugin() {
        LOGGER.trace("Creating MockPlugin");
        finishedFuture = SettableFuture.create();
        LOGGER.debug("mockPlugin: {}", System.identityHashCode(this));
    }

    @Override
    public void onRadioHeadConnected(ConnectionAdapter connection) {
        LOGGER.debug("onRadioHeadConnected: {}", connection);
        this.adapter = connection;
        connection.setMessageListener(this);
        connection.setMessageExtListener(this);
        connection.setSystemListener(this);
        connection.setConnectionReadyListener(this);
    }

    @Override
    public boolean accept(InetAddress radioHeadAddress) {
        LOGGER.debug("MockPlugin.accept(): {}", radioHeadAddress.toString());

        return true;
    }

    @Override
    public void onStateChange(StateChange notification) {
        LOGGER.debug("StateChange message received"); 
    }

    @Override
    public void onFaultInd(FaultInd notification) {
        LOGGER.debug("FaultInd message received");
    }

    @Override
    public void onHelloMessage(HelloMessage notification) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.debug("MockPlugin.onHelloMessage().run() Hello message received");
                HelloInputBuilder hib = new HelloInputBuilder();
                hib.setXid(0L);
                hib.setResult(OriHelloAckRes.valueOf("SUCCESS"));
                HelloInput hi = hib.build();
                adapter.hello(hi);
                LOGGER.debug("hello msg sent");
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getTime();
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getParam();
                    }
                }).start();
            }
        }).start();
    }
    
    protected void getTime(){
        SetTimeInputBuilder setTimeBuilder = new SetTimeInputBuilder();
        setTimeBuilder.setXid(1L);
        setTimeBuilder.setNewTime(new XsdDateTime("2012-04-26T10:23:00-05:00"));
        SetTimeInput setTimeInput = setTimeBuilder.build();
        
        try {
            LOGGER.debug("Requesting setTimeReq ");
            RpcResult<SetTimeOutput> rpcResult = adapter.setTime(
                    setTimeInput).get(2500, TimeUnit.MILLISECONDS);
            if (rpcResult.isSuccessful()) {
                String rel = rpcResult.getResult().toString();
                LOGGER.debug("rpcResult: {}", rel);
            } else {
                RpcError rpcError = rpcResult.getErrors().iterator().next();
                LOGGER.warn("rpcResult failed", rpcError.getCause());
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("getTime() exception caught: ", e.getMessage(), e);
        }
    }

    protected void getParam(){
        
        GetParamInputBuilder builder = new GetParamInputBuilder();
        ObjBuilder objbuilder = new ObjBuilder();       
        ParamBuilder parambuilder = new ParamBuilder();
        
        List<Param> paramlist = new ArrayList();
        List<Obj> objlist = new ArrayList();
        
        builder.setXid(2L);
            objbuilder.setId(new ObjId("ALL"));
                parambuilder.setName("ALL");
                paramlist.add(parambuilder.build());
            objbuilder.setParam(paramlist);                    
            objlist.add(objbuilder.build());
        builder.setObj(objlist);        
        
        GetParamInput getParamInput = builder.build();
        
        try {
            LOGGER.debug("Requesting getParamReq ");
            RpcResult<GetParamOutput> rpcResult = adapter.getParam(
                    getParamInput).get(2500, TimeUnit.MILLISECONDS);
            if (rpcResult.isSuccessful()) {
                String rel = rpcResult.getResult().toString();
                LOGGER.debug("rpcResult: {}", rel);
            } else {
                RpcError rpcError = rpcResult.getErrors().iterator().next();
                LOGGER.warn("rpcResult failed", rpcError.getCause());
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("getParam() exception caught: ", e.getMessage(), e);
        }
    }
    
    protected void shutdown() {
        try {
            LOGGER.debug("MockPlugin.shutdown() sleeping 5... : {}", System.identityHashCode(this));
            Thread.sleep(500);
            if (adapter != null) {
                Future<Boolean> disconnect = adapter.disconnect();
                disconnect.get();
                LOGGER.debug("MockPlugin.shutdown() Disconnected");
            }
        } catch (Exception e) {
            LOGGER.error("MockPlugin.shutdown() exception caught: ", e.getMessage(), e);
        }
        finishedFuture.set(null);
    }

    @Override
    public void onDisconnectEvent(DisconnectEvent notification) {
        LOGGER.debug("disconnection occured: {}", notification.getInfo());
    }

    /**
     * @return finishedFuture object
     */
    public SettableFuture<Void> getFinishedFuture() {
        return finishedFuture;
    }

    @Override
    public void onRadioHeadIdleEvent(RadioHeadIdleEvent notification) {
        LOGGER.debug("MockPlugin.onRadioHeadIdleEvent() radioHead status: {}", notification.getInfo());
        idleCounter ++;
    }

    /**
     * @return number of occured idleEvents
     */
    public int getIdleCounter() {
        return idleCounter;
    }

    @Override
    public void onConnectionReady() {
        LOGGER.trace("MockPlugin().onConnectionReady()");
    }

    /**
     * Initiates connection to device
     * @param radioHeadConnectionProvider
     * @param host - host IP
     * @param port - port number
     */
    public void initiateConnection(RadioHeadConnectionProviderImpl radioHeadConnectionProvider, String host, int port) {
        LOGGER.trace("MockPlugin().initiateConnection()");
        radioHeadConnectionProvider.initiateConnection(host, port);
    }
}
