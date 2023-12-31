package net.jmp.demo.reactive.streams.org;

/*
 * (#)IntegerSubscriber.java    0.7.0   01/03/2024
 * (#)IntegerSubscriber.java    0.6.0   01/02/2024
 * (#)IntegerSubscriber.java    0.5.0   12/28/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.7.0
 * @since     0.5.0
 */

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.*;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class IntegerSubscriber extends WaitableSubscriber<Integer> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final List<Integer> consumedIntegers = new ArrayList<>();
    private Subscription subscription;

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
    public void onNext(final Integer item) {
        this.logger.info("onNext: {}", item);

        if (item == null)
            throw new NullPointerException("Null item received by onNext");

        this.consumedIntegers.add(item);
    }

    @Override
    public void onError(Throwable throwable) {
        this.logger.error("onError: {}", throwable.getMessage());
    }

    @Override
    public void onComplete() {
        this.logger.info("onComplete");

        super.onComplete();
    }

    public List<Integer> getConsumedIntegers() {
        return this.consumedIntegers;
    }
}
