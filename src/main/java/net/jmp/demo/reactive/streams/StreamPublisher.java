package net.jmp.demo.reactive.streams;

import java.util.Iterator;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import java.util.function.Supplier;

import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

class StreamPublisher<T> implements Publisher<T> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    private final Supplier<Stream<T>> streamSupplier;

    StreamPublisher(final Supplier<Stream<T>> streamSupplier) {
        super();

        this.streamSupplier = streamSupplier;
    }

    @Override
    public void subscribe(final Subscriber<? super T> subscriber) {
        this.logger.entry(subscriber);

        final var subscription = new StreamSubscription(subscriber);

        subscriber.onSubscribe(subscription);
        subscription.doOnSubscribed();

        this.logger.exit();
    }

    private class StreamSubscription implements Subscription {
        private final Subscriber<? super T> subscriber;
        private final Iterator<? extends T> iterator;
        private final AtomicBoolean isTerminated = new AtomicBoolean(false);
        private final AtomicLong demand = new AtomicLong();
        private final AtomicReference<Throwable> error = new AtomicReference<>();

        StreamSubscription(final Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;

            Iterator<? extends T> streamIterator = null;

            try {
                streamIterator = streamSupplier.get().iterator();
            } catch (final Exception e) {
                error.set(e);
            }

            this.iterator = streamIterator;
        }

        @Override
        public void request(final long elements) {
            if (elements <= 0 && !terminate()) {
                this.subscriber.onError(new IllegalArgumentException("Negative subscription request"));

                return;
            }

            if (this.demand.get() > 0) {
                this.demand.getAndAdd(elements);

                return;
            }

            this.demand.getAndAdd(elements);

            for (; this.demand.get() > 0 && this.iterator.hasNext() && !this.isTerminated(); this.demand.decrementAndGet()) {
                try {
                    this.subscriber.onNext(this.iterator.next());
                } catch (final Exception e) {
                    if (!terminate()) {
                        this.subscriber.onError(e);
                    }
                }
            }

            if(!this.iterator.hasNext()) {
                this.subscriber.onComplete();
            }
        }

        void doOnSubscribed() {
            final Throwable throwable = error.get();

            if (throwable != null && !terminate()) {
                this.subscriber.onError(throwable);
            }
        }

        @Override
        public void cancel() {
            this.terminate();
        }

        private boolean terminate() {
            return this.isTerminated.getAndSet(true);
        }

        private boolean isTerminated() {
            return this.isTerminated.get();
        }
    }
}
