package net.jmp.demo.reactive.streams.flow;

/*
 * (#)ListSubscriber.java   0.4.0   12/28/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.4.0
 */

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class ListSubscriber<T> implements Subscriber<T> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    private Subscription subscription;

    public ListSubscriber() {
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
    public void onNext(final T item) {
        logger.info("onNext: {}", item);

        if (item == null)
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
