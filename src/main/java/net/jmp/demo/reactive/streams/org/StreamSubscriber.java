package net.jmp.demo.reactive.streams.org;

/*
 * (#)StreamSubscriber.java 0.7.0   01/03/2024
 * (#)StreamSubscriber.java 0.6.0   01/02/2024
 * (#)StreamSubscriber.java 0.4.0   12/28/2023
 * (#)StreamSubscriber.java 0.2.0   12/25/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.7.0
 * @since     0.2.0
 */

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Subscription;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class StreamSubscriber<T> extends WaitableSubscriber<T> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final List<T> consumedElements = new ArrayList<>();
    private Subscription subscription;

    public StreamSubscriber() {
        super();
    }

    @Override
    public void onSubscribe(final Subscription subscription) {
        this.logger.info("onSubscribe");

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
        this.logger.info("onNext: {}", t);

        if (t == null)
            throw new NullPointerException("Null element received by onNext");

        this.consumedElements.add(t);
    }

    @Override
    public void onError(final Throwable throwable) {
        this.logger.error("onError: {}", throwable.getMessage());
    }

    @Override
    public void onComplete() {
        this.logger.info("onComplete");

        super.onComplete();
    }

    public List<T> getConsumedElements() {
        return this.consumedElements;
    }
}
