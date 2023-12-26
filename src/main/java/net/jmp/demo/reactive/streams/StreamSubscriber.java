package net.jmp.demo.reactive.streams;

/*
 * (#)StreamSubscriber.java 0.2.0   12/25/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.2.0
 * @since     0.2.0
 */

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

class StreamSubscriber<T> implements Subscriber<T> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    private Subscription subscription;

    StreamSubscriber() {
        super();
    }

    @Override
    public void onSubscribe(final Subscription subscription) {
        logger.info("onSubscribe");

        if (this.subscription == null) {
            this.subscription = subscription;

            subscription.request(Long.MAX_VALUE);
        } else {
            this.subscription = null;

            subscription.cancel();
        }
    }

    @Override
    public void onNext(final T t) {
        logger.info("onNext: {}", t);

        if (t == null)
            throw new NullPointerException("Null element received by onNext");
    }

    @Override
    public void onError(final Throwable throwable) {
        logger.error("onError: {}", throwable.getMessage());
    }

    @Override
    public void onComplete() {
        logger.info("onComplete");
    }
}
