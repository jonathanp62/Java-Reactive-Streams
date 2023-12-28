package net.jmp.demo.reactive.streams.flow;

/*
 * (#)ListPublisher.java    0.4.0   12/28/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.4.0
 */

import java.util.Iterator;
import java.util.List;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import java.util.function.Supplier;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class ListPublisher<T> implements Publisher<T>, AutoCloseable {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    private final ExecutorService executor = ForkJoinPool.commonPool();

    private final Supplier<List<T>> listSupplier;

    private boolean isSubscribed;

    public ListPublisher(final Supplier<List<T>> listSupplier) {
        super();

        this.listSupplier = listSupplier;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        this.logger.entry(subscriber);

        if (!this.isSubscribed) {
            final var subscription = new ListSubscription(subscriber, this.executor);

            subscriber.onSubscribe(subscription);
            subscription.doOnSubscribed();

            this.isSubscribed = true;
        } else {
            subscriber.onError(new IllegalStateException("Already subscribed"));
        }

        this.logger.exit();
    }

    @Override
    public void close() {
        if (!this.executor.isShutdown())
            this.executor.shutdown();
    }

    private class ListSubscription implements Subscription {
        private final Subscriber<? super T> subscriber;
        private final ExecutorService executor;
        private final Iterator<? extends T> iterator;
        private final AtomicBoolean isTerminated = new AtomicBoolean(false);
        private final AtomicLong demand = new AtomicLong();
        private final AtomicReference<Throwable> error = new AtomicReference<>();

        private Future<?> future;   // To allow cancellation

        ListSubscription(final Subscriber<? super T> subscriber, final ExecutorService executor) {
            this.subscriber = subscriber;
            this.executor = executor;

            Iterator<? extends T> listIterator = null;

            try {
                listIterator = listSupplier.get().iterator();
            } catch (final Exception e) {
                error.set(e);
            }

            this.iterator = listIterator;
        }

        @Override
        public void request(final long elements) {
            if (elements <= 0 && !terminate()) {
                this.executor.execute(() -> this.subscriber.onError(new IllegalArgumentException("Negative subscription request")));

                return;
            }

            while (true) {
                long currentDemand = this.demand.getAcquire();

                if (currentDemand == Long.MAX_VALUE) {
                    return;
                }

                long adjustedDemand = currentDemand + elements;

                if (adjustedDemand < 0L) {
                    adjustedDemand = Long.MAX_VALUE;
                }

                if (this.demand.compareAndSet(currentDemand, adjustedDemand)) {
                    if (currentDemand > 0) {
                        return;
                    }

                    break;
                }
            }

            for (; this.demand.get() > 0 && this.iterator.hasNext() && !this.isTerminated(); this.demand.decrementAndGet()) {
                try {
                    this.future = this.executor.submit(() -> this.subscriber.onNext(this.iterator.next()));
                } catch (final Exception e) {
                    if (!terminate()) {
                        this.executor.execute(() -> this.subscriber.onError(e));
                    }
                }
            }

            if(!this.iterator.hasNext()) {
                this.executor.execute(this.subscriber::onComplete); // () -> this.subscriber.onComplete()
            }
        }

        void doOnSubscribed() {
            final Throwable throwable = error.get();

            if (throwable != null && !terminate()) {
                this.executor.execute(() -> this.subscriber.onError(throwable));
            }
        }

        @Override
        public void cancel() {
            this.terminate();

            if (this.future != null)
                this.future.cancel(false);
        }

        private boolean terminate() {
            return this.isTerminated.getAndSet(true);
        }

        private boolean isTerminated() {
            return this.isTerminated.get();
        }
    }
}
