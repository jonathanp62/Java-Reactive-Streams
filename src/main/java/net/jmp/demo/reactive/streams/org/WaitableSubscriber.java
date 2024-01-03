package net.jmp.demo.reactive.streams.org;

/*
 * (#)WaitableSubscriber.java   0.7.0   01/03/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.7.0
 * @since     0.7.0
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Subscriber;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public abstract class WaitableSubscriber<T> implements Subscriber<T> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    protected WaitableSubscriber() {
        super();
    }

    @Override
    public void onComplete() {
        this.logger.info("Waitable: onComplete");

        this.countDownLatch.countDown();
    }

    public WaitableSubscriber<T> await() {
        return this.await(2, TimeUnit.SECONDS);
    }

    public WaitableSubscriber<T> await(final long timeout, final TimeUnit unit) {
        this.logger.info("Waitable: await");

        try {
            if (!this.countDownLatch.await(timeout, unit))
                throw new RuntimeException("Timed out waiting for the publisher to complete");
        } catch (final InterruptedException ie) {
            this.logger.catching(ie);

            Thread.currentThread().interrupt();

            throw new RuntimeException("Interrupted waiting for the publisher to complete");
        }

        return this;
    }
}
