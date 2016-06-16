/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2016 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */


package org.opendaylight.ocpjava.protocol.impl.core.connection;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.opendaylight.ocpjava.protocol.api.connection.ConnectionReadyListener;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueueHandler;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueueHandlerRegistration;
import org.opendaylight.ocpjava.protocol.impl.core.ChannelInitializerFactory;

import org.opendaylight.ocpjava.statistics.CounterEventTypes;
import org.opendaylight.ocpjava.statistics.OcpStatisticsCounters;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.SetTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ReResetOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.CreateObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.DeleteObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.GetFaultOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.StateChangeInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.FaultInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.protocol.rev150811.OcpProtocolListener;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloMessage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.ReDirectInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.ReDirectOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.OcpExtensionListener;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.DisconnectEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.RadioHeadIdleEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.system.rev150811.SystemNotificationsListener;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.Notification;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles messages (notifications + rpcs) and connections
 * 
 * @author mirehak
 * @author michal.polkorab
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 * 
 */
public class ConnectionAdapterImpl implements ConnectionFacade {
    /** after this time, RPC future response objects will be thrown away (in minutes) */
    public static final int RPC_RESPONSE_EXPIRATION = 1;

    /**
     * Default depth of write queue, e.g. we allow these many messages
     * to be queued up before blocking producers.
     */
    public static final int DEFAULT_QUEUE_DEPTH = 1024;

    private static final Logger LOG = LoggerFactory
            .getLogger(ConnectionAdapterImpl.class);
    private static final Exception QUEUE_FULL_EXCEPTION =
            new RejectedExecutionException("Output queue is full");

    private static final RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>> REMOVAL_LISTENER =
            new RemovalListener<RpcResponseKey, ResponseExpectedRpcListener<?>>() {
        @Override
        public void onRemoval(
                final RemovalNotification<RpcResponseKey, ResponseExpectedRpcListener<?>> notification) {
            if (! notification.getCause().equals(RemovalCause.EXPLICIT)) {
                notification.getValue().discard();
            }
        }
    };


    /** expiring cache for future rpcResponses */
    private Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> responseCache;

    private final ChannelOutboundQueue output;
    private final Channel channel;

    private SystemNotificationsListener systemListener;
    private OcpProtocolListener messageListener;
    private OcpExtensionListener messageExtListener;

    private ConnectionReadyListener connectionReadyListener;

    private OutboundQueueManager<?> outputManager;
    private boolean disconnectOccured = false;
    private final OcpStatisticsCounters statisticsCounters;
    private final InetSocketAddress address;

    /**
     * default ctor
     * @param channel the channel to be set - used for communication
     * @param address client address (used only in case of UDP communication,
     *  as there is no need to store address over tcp (stable channel))
     */
    public ConnectionAdapterImpl(final Channel channel, final InetSocketAddress address) {
        responseCache = CacheBuilder.newBuilder()
                .concurrencyLevel(1)
                .expireAfterWrite(RPC_RESPONSE_EXPIRATION, TimeUnit.MINUTES)
                .removalListener(REMOVAL_LISTENER).build();

        this.channel = Preconditions.checkNotNull(channel);
        this.output = new ChannelOutboundQueue(channel, DEFAULT_QUEUE_DEPTH, address);
        this.address = address;
        channel.pipeline().addLast(output);
        statisticsCounters = OcpStatisticsCounters.getInstance();

        LOG.debug("ConnectionAdapter created");
    }

    @Override
    public Future<RpcResult<HealthCheckOutput>> healthCheck(final HealthCheckInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, HealthCheckOutput.class, "health-check-input sending failed");
    }

    @Override
    public Future<RpcResult<SetTimeOutput>> setTime(final SetTimeInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, SetTimeOutput.class, "set-time-input sending failed");
    }

    @Override
    public Future<RpcResult<ReResetOutput>> reReset(final ReResetInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, ReResetOutput.class, "re-reset-input sending failed");
    }

    @Override
    public Future<RpcResult<GetParamOutput>> getParam(final GetParamInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, GetParamOutput.class, "get-param-input sending failed");
    }

    @Override
    public Future<RpcResult<ModifyParamOutput>> modifyParam(final ModifyParamInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, ModifyParamOutput.class, "modify-param-input sending failed");
    }

    @Override
    public Future<RpcResult<CreateObjOutput>> createObj(final CreateObjInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, CreateObjOutput.class, "create-obj-input sending failed");
    }    
    
    @Override
    public Future<RpcResult<DeleteObjOutput>> deleteObj(final DeleteObjInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, DeleteObjOutput.class, "delete-object-input sending failed");
    }
    
    @Override
    public Future<RpcResult<GetStateOutput>> getState(final GetStateInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, GetStateOutput.class, "get-state-input sending failed");
    }

    @Override
    public Future<RpcResult<ModifyStateOutput>> modifyState(final ModifyStateInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, ModifyStateOutput.class, "modify-state-input sending failed");
    }

    @Override
    public Future<RpcResult<GetFaultOutput>> getFault(final GetFaultInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, GetFaultOutput.class, "get-fault-input sending failed");
    }

    @Override
    public Future<RpcResult<ReDirectOutput>> reDirect(final ReDirectInput input) {
        return sendToRadioHeadExpectRpcResultFuture(
                input, ReDirectOutput.class, "redirect-input sending failed");
    }


    @Override
    public Future<RpcResult<Void>> hello(final HelloInput input) {
        return sendToRadioHeadFuture(input, "hello-ack sending failed");
    }    
    

    @Override
    public Future<Boolean> disconnect() {
        ChannelFuture disconnectResult = channel.disconnect();
        responseCache.invalidateAll();
        disconnectOccured = true;

        return handleTransportChannelFuture(disconnectResult);
    }

    @Override
    public boolean isAlive() {
        return channel.isOpen();
    }

    @Override
    public void setMessageListener(final OcpProtocolListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void setMessageExtListener(final OcpExtensionListener messageExtListener) {
        this.messageExtListener = messageExtListener;
    }


    @Override
    public void consume(final DataObject message) {
        LOG.debug("ConsumeIntern msg on {}", channel);
        if (disconnectOccured ) {
            return;
        }
        if (message instanceof Notification) {

            // System events
            if (message instanceof DisconnectEvent) {
                systemListener.onDisconnectEvent((DisconnectEvent) message);
                responseCache.invalidateAll();
                disconnectOccured = true;
            } else if (message instanceof RadioHeadIdleEvent) {
                systemListener.onRadioHeadIdleEvent((RadioHeadIdleEvent) message);

            // OCP messages
            } else if (message instanceof StateChangeInd) {
                LOG.info("StateChangeInd received / branch");
                messageListener.onStateChangeInd((StateChangeInd) message);
                statisticsCounters.incrementCounter(CounterEventTypes.US_MESSAGE_PASS);

            } else if (message instanceof FaultInd) {
                LOG.info("FaultInd received / branch");
                messageListener.onFaultInd((FaultInd) message);
                statisticsCounters.incrementCounter(CounterEventTypes.US_MESSAGE_PASS);

            } else if (message instanceof HelloMessage) {
                LOG.info("Hello received / branch");
                messageExtListener.onHelloMessage((HelloMessage) message);
                statisticsCounters.incrementCounter(CounterEventTypes.US_MESSAGE_PASS);
                
            } else {
                LOG.warn("message listening not supported for type: {}", message.getClass());
            }
        } else if (message instanceof OcpHeader) {
            LOG.debug("OCPheader msg received");

            if (outputManager == null || !outputManager.onMessage((OcpHeader) message)) {
                RpcResponseKey key = createRpcResponseKey((OcpHeader) message);
                final ResponseExpectedRpcListener<?> listener = findRpcResponse(key);
                if (listener != null) {
                    LOG.debug("corresponding rpcFuture found");
                    listener.completed((OcpHeader)message);
                    statisticsCounters.incrementCounter(CounterEventTypes.US_MESSAGE_PASS);
                    LOG.debug("after setting rpcFuture");
                    responseCache.invalidate(key);
                } else {
                    LOG.warn("received unexpected rpc response: {}", key);
                }
            }
        } else {
            LOG.warn("message listening not supported for type: {}", message.getClass());
        }
    }

    private <T> ListenableFuture<RpcResult<T>> enqueueMessage(final AbstractRpcListener<T> promise) {
        LOG.debug("Submitting promise {}", promise);

        if (!output.enqueue(promise)) {
            LOG.debug("Message queue is full, rejecting execution");
            promise.failedRpc(QUEUE_FULL_EXCEPTION);
        } else {
            LOG.debug("Promise enqueued successfully");
        }

        return promise.getResult();
    }

    /**
     * sends given message to radioHead, sending result will be reported via return value
     * @param input message to send
     * @param failureInfo describes, what type of message caused failure by sending
     * @return future object, <ul>
     *  <li>if send successful, {@link RpcResult} without errors and successful
     *  status will be returned, </li>
     *  <li>else {@link RpcResult} will contain errors and failed status</li>
     *  </ul>
     */
    private ListenableFuture<RpcResult<Void>> sendToRadioHeadFuture(
            final DataObject input, final String failureInfo) {
        statisticsCounters.incrementCounter(CounterEventTypes.DS_ENTERED_OCPJAVA);
        return enqueueMessage(new SimpleRpcListener(input, failureInfo));
    }

    /**
     * sends given message to radioHead, sending result or radioHead response will be reported via return value
     * @param input message to send
     * @param responseClazz type of response
     * @param failureInfo describes, what type of message caused failure by sending
     * @return future object, <ul>
     *  <li>if send fails, {@link RpcResult} will contain errors and failed status </li>
     *  <li>else {@link RpcResult} will be stored in responseCache and wait for particular timeout
     *  ({@link ConnectionAdapterImpl#RPC_RESPONSE_EXPIRATION}),
     *  <ul><li>either radioHead will manage to answer
     *  and then corresponding response message will be set into returned future</li>
     *  <li>or response in cache will expire and returned future will be cancelled</li></ul>
     *  </li>
     *  </ul>
     */
    private <IN extends OcpHeader, OUT extends OcpHeader> ListenableFuture<RpcResult<OUT>> sendToRadioHeadExpectRpcResultFuture(
            final IN input, final Class<OUT> responseClazz, final String failureInfo) {
        final RpcResponseKey key = new RpcResponseKey(input.getXid(), responseClazz.getName());
        final ResponseExpectedRpcListener<OUT> listener =
                new ResponseExpectedRpcListener<>(input, failureInfo, responseCache, key);
        statisticsCounters.incrementCounter(CounterEventTypes.DS_ENTERED_OCPJAVA);
        return enqueueMessage(listener);
    }

    /**
     * @param resultFuture
     * @param failureInfo
     * @param errorSeverity
     * @param message
     * @return
     */
    private static SettableFuture<Boolean> handleTransportChannelFuture(
            final ChannelFuture resultFuture) {

        final SettableFuture<Boolean> transportResult = SettableFuture.create();

        resultFuture.addListener(new GenericFutureListener<io.netty.util.concurrent.Future<? super Void>>() {

            @Override
            public void operationComplete(
                    final io.netty.util.concurrent.Future<? super Void> future)
                    throws Exception {
                transportResult.set(future.isSuccess());
                if (!future.isSuccess()) {
                    transportResult.setException(future.cause());
                }
            }
        });
        return transportResult;
    }

    /**
     * @param message
     * @return
     */
    private static RpcResponseKey createRpcResponseKey(final OcpHeader message) {
        return new RpcResponseKey(message.getXid(), message.getImplementedInterface().getName());
    }

    /**
     * @return
     */
    private ResponseExpectedRpcListener<?> findRpcResponse(final RpcResponseKey key) {
        return responseCache.getIfPresent(key);
    }

    @Override
    public void setSystemListener(final SystemNotificationsListener systemListener) {
        this.systemListener = systemListener;
    }

    @Override
    public void checkListeners() {
        final StringBuilder buffer =  new StringBuilder();
        if (systemListener == null) {
            buffer.append("SystemListener ");
        }
        if (messageListener == null) {
            buffer.append("MessageListener ");
        }
        if (messageExtListener == null) {
            buffer.append("MessageExtListener ");
        }
        if (connectionReadyListener == null) {
            buffer.append("ConnectionReadyListener ");
        }

        Preconditions.checkState(buffer.length() == 0, "Missing listeners: %s", buffer.toString());
    }

    @Override
    public void fireConnectionReadyNotification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectionReadyListener.onConnectionReady();
            }
        }).start();
    }

    @Override
    public void setConnectionReadyListener(
            final ConnectionReadyListener connectionReadyListener) {
        this.connectionReadyListener = connectionReadyListener;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    /**
     * Used only for testing purposes
     * @param cache
     */
    public void setResponseCache(final Cache<RpcResponseKey, ResponseExpectedRpcListener<?>> cache) {
        this.responseCache = cache;
    }

    @Override
    public boolean isAutoRead() {
    	return channel.config().isAutoRead();
    }

    @Override
    public void setAutoRead(final boolean autoRead) {
    	channel.config().setAutoRead(autoRead);
    }

    @Override
    public void setIdleTimeout(final long idleTimeout) {
        ChannelInitializerFactory channelInitializerFactory = new ChannelInitializerFactory();
    	channelInitializerFactory.setRadioHeadIdleTimeout(idleTimeout);
    }

    @Override
    public <T extends OutboundQueueHandler> OutboundQueueHandlerRegistration<T> registerOutboundQueueHandler(
            final T handler, final int maxQueueDepth) {
        Preconditions.checkState(outputManager == null, "Manager %s already registered", outputManager);

        final OutboundQueueManager<T> ret = new OutboundQueueManager<>(this, address, handler, maxQueueDepth);
        outputManager = ret;
        channel.pipeline().addLast(outputManager);

        return new OutboundQueueHandlerRegistrationImpl<T>(handler) {
            @Override
            protected void removeRegistration() {
                outputManager.close();
                channel.pipeline().remove(outputManager);
                outputManager = null;
            }
        };
    }

    Channel getChannel() {
        return channel;
    }

}
