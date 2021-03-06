/*
 * Copyright (c) 2015 Pantheon Technologies s.r.o. and others. All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpjava.protocol.impl.core.connection;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.opendaylight.ocpjava.protocol.api.connection.OutboundQueueHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OcpHeader;

final class OutboundQueueManager<T extends OutboundQueueHandler> extends ChannelInboundHandlerAdapter implements AutoCloseable {
    private static enum PipelineState {
        /**
         * Netty thread is potentially idle, no assumptions
         * can be made about its state.
         */
        IDLE,
        /**
         * Netty thread is currently reading, once the read completes,
         * if will flush the queue in the {@link #FLUSHING} state.
         */
        READING,
        /**
         * Netty thread is currently performing a flush on the queue.
         * It will then transition to {@link #IDLE} state.
         */
        WRITING,
    }

    private static final Logger LOG = LoggerFactory.getLogger(OutboundQueueManager.class);

    /**
     * Default low write watermark. Channel will become writable when number of outstanding
     * bytes dips below this value.
     */
    private static final int DEFAULT_LOW_WATERMARK = 128 * 1024;

    /**
     * Default write high watermark. Channel will become un-writable when number of
     * outstanding bytes hits this value.
     */
    private static final int DEFAULT_HIGH_WATERMARK = DEFAULT_LOW_WATERMARK * 2;

    private final AtomicBoolean flushScheduled = new AtomicBoolean();
    private final StackedOutboundQueue currentQueue;
    private final ConnectionAdapterImpl parent;
    private final InetSocketAddress address;
    private final int maxNonBarrierMessages;
    private final T handler;

    // Accessed concurrently
    private volatile PipelineState state = PipelineState.IDLE;

    // Updated from netty only
    private boolean alreadyReading;
    private boolean shuttingDown;

    // Passed to executor to request triggering of flush
    private final Runnable flushRunnable = new Runnable() {
        @Override
        public void run() {
            flush();
        }
    };

    OutboundQueueManager(final ConnectionAdapterImpl parent, final InetSocketAddress address, final T handler,
        final int maxNonBarrierMessages) {
        this.parent = Preconditions.checkNotNull(parent);
        this.handler = Preconditions.checkNotNull(handler);
        Preconditions.checkArgument(maxNonBarrierMessages > 0);
        this.maxNonBarrierMessages = maxNonBarrierMessages;
        this.address = address;

        currentQueue = new StackedOutboundQueue(this);
        LOG.debug("Queue manager instantiated with queue {}", currentQueue);
        handler.onConnectionQueueChanged(currentQueue);
    }

    T getHandler() {
        return handler;
    }

    @Override
    public void close() {
        handler.onConnectionQueueChanged(null);
    }

    /**
     * Invoked whenever a message comes in from the radioHead. Runs matching
     * on all active queues in an attempt to complete a previous request.
     *
     * @param message Potential response message
     * @return True if the message matched a previous request, false otherwise.
     */
    boolean onMessage(final OcpHeader message) {
        LOG.trace("Attempting to pair message {} to a request", message);

        return currentQueue.pairRequest(message);
    }

    private void scheduleFlush() {
        if (flushScheduled.compareAndSet(false, true)) {
            LOG.trace("Scheduling flush task on channel {}", parent.getChannel());
            parent.getChannel().eventLoop().execute(flushRunnable);
        } else {
            LOG.trace("Flush task is already present on channel {}", parent.getChannel());
        }
    }


    private void rescheduleFlush() {
        /*
         * We are almost ready to terminate. This is a bit tricky, because
         * we do not want to have a race window where a message would be
         * stuck on the queue without a flush being scheduled.
         *
         * So we mark ourselves as not running and then re-check if a
         * flush out is needed. That will re-synchronized with other threads
         * such that only one flush is scheduled at any given time.
         */
        if (!flushScheduled.compareAndSet(true, false)) {
            LOG.warn("Channel {} queue {} flusher found unscheduled", parent.getChannel(), this);
        }

        conditionalFlush();
    }

    private void writeAndFlush() {
        state = PipelineState.WRITING;

        final long start = System.nanoTime();

        final int entries = currentQueue.writeEntries(parent.getChannel(), start);
        if (entries > 0) {
            LOG.trace("Flushing channel {}", parent.getChannel());
            parent.getChannel().flush();
        }

        if (LOG.isDebugEnabled()) {
            final long stop = System.nanoTime();
            LOG.debug("Flushed {} messages to channel {} in {}us", entries,
                parent.getChannel(), TimeUnit.NANOSECONDS.toMicros(stop - start));
        }

        state = PipelineState.IDLE;
    }

    /**
     * Perform a single flush operation. We keep it here so we do not generate
     * syntetic accessors for private fields. Otherwise it could be moved into
     * {@link #flushRunnable}.
     */
    protected void flush() {
        // If the channel is gone, just flush whatever is not completed
        if (!shuttingDown) {
            LOG.trace("Dequeuing messages to channel {}", parent.getChannel());
            writeAndFlush();
            rescheduleFlush();
        } else if (currentQueue.finishShutdown()) {
            handler.onConnectionQueueChanged(null);
            LOG.debug("Channel {} shutdown complete", parent.getChannel());
        } else {
            LOG.trace("Channel {} current queue not completely flushed yet", parent.getChannel());
            rescheduleFlush();
        }
    }

    /**
     * Schedule a queue flush if it is not empty and the channel is found
     * to be writable. May only be called from Netty context.
     */
    private void conditionalFlush() {
        if (currentQueue.needsFlush()) {
            if (shuttingDown || parent.getChannel().isWritable()) {
                scheduleFlush();
            } else {
                LOG.debug("Channel {} is not I/O ready, not scheduling a flush", parent.getChannel());
            }
        } else {
            LOG.trace("Queue is empty, no flush needed");
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        conditionalFlush();
    }

    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        /*
         * Tune channel write buffering. We increase the writability window
         * to ensure we can flush an entire queue segment in one go. We definitely
         * want to keep the difference above 64k, as that will ensure we use jam-packed
         * TCP packets. UDP will fragment as appropriate.
         */
        ctx.channel().config().setWriteBufferHighWaterMark(DEFAULT_HIGH_WATERMARK);
        ctx.channel().config().setWriteBufferLowWaterMark(DEFAULT_LOW_WATERMARK);

        super.handlerAdded(ctx);
    }

    @Override
    public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);

        // The channel is writable again. There may be a flush task on the way, but let's
        // steal its work, potentially decreasing latency. Since there is a window between
        // now and when it will run, it may still pick up some more work to do.
        LOG.debug("Channel {} writability changed, invoking flush", parent.getChannel());
        writeAndFlush();
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        LOG.debug("Channel {} initiating shutdown...", ctx.channel());

        shuttingDown = true;
        final long entries = currentQueue.startShutdown(ctx.channel());
        LOG.debug("Cleared {} queue entries from channel {}", entries, ctx.channel());

        scheduleFlush();
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        // Netty does not provide a 'start reading' callback, so this is our first
        // (and repeated) chance to detect reading. Since this callback can be invoked
        // multiple times, we keep a boolean we check. That prevents a volatile write
        // on repeated invocations. It will be cleared in channelReadComplete().
        if (!alreadyReading) {
            alreadyReading = true;
            state = PipelineState.READING;
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);

        // Run flush regardless of writability. This is not strictly required, as
        // there may be a scheduled flush. Instead of canceling it, which is expensive,
        // we'll steal its work. Note that more work may accumulate in the time window
        // between now and when the task will run, so it may not be a no-op after all.
        //
        // The reason for this is to will the output buffer before we go into selection
        // phase. This will make sure the pipe is full (in which case our next wake up
        // will be the queue becoming writable).
        writeAndFlush();
    }

    @Override
    public String toString() {
        return String.format("Channel %s queue [flushing=%s]", parent.getChannel(), flushScheduled.get());
    }

    void ensureFlushing() {
        // If the channel is not writable, there's no point in waking up,
        // once we become writable, we will run a full flush
        if (!parent.getChannel().isWritable()) {
            return;
        }

        // We are currently reading something, just a quick sync to ensure we will in fact
        // flush state.
        final PipelineState localState = state;
        LOG.debug("Synchronize on pipeline state {}", localState);
        switch (localState) {
        case READING:
            // Netty thread is currently reading, it will flush the pipeline once it
            // finishes reading. This is a no-op situation.
            break;
        case WRITING:
        case IDLE:
        default:
            // We cannot rely on the change being flushed, schedule a request
            scheduleFlush();
        }
    }

    /**
     * Write a message into the underlying channel.
     *
     * @param now Time reference for 'now'. We take this as an argument, as
     *            we need a timestamp to mark barrier messages we see swinging
     *            by. That timestamp does not need to be completely accurate,
     *            hence we use the flush start time. Alternative would be to
     *            measure System.nanoTime() for each barrier -- needlessly
     *            adding overhead.
     */
    void writeMessage(final OcpHeader message, final long now) {
        final Object wrapper = new MessageListenerWrapper(message, null);
        parent.getChannel().write(wrapper);
    }
}
