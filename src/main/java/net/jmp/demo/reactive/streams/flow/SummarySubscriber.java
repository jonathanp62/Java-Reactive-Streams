package net.jmp.demo.reactive.streams.flow;

/*
 * (#)SummarySubscriber.java    0.7.0   01/04/2023
 * (#)SummarySubscriber.java    0.6.0   01/01/2023
 * (#)SummarySubscriber.java    0.4.0   12/28/2023
 * (#)SummarySubscriber.java    0.3.0   12/27/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.7.0
 * @since     0.3.0
 */

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.Flow.Subscription;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class SummarySubscriber extends WaitableSubscriber<Summary> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final List<Summary> consumedSummaries = new ArrayList<>();

    private Subscription subscription;

    public SummarySubscriber() {
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
    public void onNext(final Summary summary) {
        logger.info("onNext: {}", summary);

        if (summary == null)
            throw new NullPointerException("Null summary received by onNext");

        this.consumedSummaries.add(summary);
    }

    @Override
    public void onError(final Throwable throwable) {
        logger.error("onError: {}", throwable.getMessage());
    }

    @Override
    public void onComplete() {
        logger.info("onComplete");

        super.onComplete();
    }

    public List<Summary> getConsumedSummaries() {
        return this.consumedSummaries;
    }
}
