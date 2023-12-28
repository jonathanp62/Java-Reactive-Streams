package net.jmp.demo.reactive.streams.org;

/*
 * (#)DoublingProcessor.java    0.5.0   12/28/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.5.0
 * @since     0.5.0
 */

import org.reactivestreams.*;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class DoublingProcessor implements Processor<Integer, Integer> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    private Subscriber<? super Integer> subscriber;
    private Subscription subscription;

    @Override
    public void subscribe(final Subscriber<? super Integer> subscriber) {
        this.logger.info("subscribe");

        this.subscriber = subscriber;

        subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(final long n) {
                // Request elements from upstream when downstream requests
                subscription.request(n);
            }

            @Override
            public void cancel() {
                // Cancel subscription when downstream cancels
                subscription.cancel();
            }
        });
    }

    @Override
    public void onSubscribe(final Subscription subscription) {
        this.logger.info("onSubscribe");

        this.subscription = subscription;

        this.subscription.request(1); // Request the first element when ready
    }

    @Override
    public void onNext(final Integer item) {
        this.logger.info("onNext: {}", item);

        if (item == null)
            throw new NullPointerException("Null item received by onNext");

        // Double the received integer
        int doubledValue = item * 2;

        // Publish the doubled value downstream
        this.subscriber.onNext(doubledValue);

        // Request the next element from upstream
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        this.logger.error("onError: {}", throwable.getMessage());

        this.subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        this.logger.info("onComplete");

        this.subscriber.onComplete();
    }
}
