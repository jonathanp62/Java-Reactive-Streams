package net.jmp.demo.reactive.streams.rxjava;

/*
 * (#)WaitableObserver.java 0.8.0   01/05/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.8.0
 * @since     0.8.0
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

abstract class WaitableObserver<T> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    protected WaitableObserver() {
        super();
    }

    protected void onComplete() {
        this.logger.entry();

        this.logger.info("Waitable: onComplete");
        this.countDownLatch.countDown();

        this.logger.exit();
    }

    public WaitableObserver<T> await() {
        return this.await(2, TimeUnit.SECONDS);
    }

    public WaitableObserver<T> await(final long timeout, final TimeUnit unit) {
        this.logger.entry(timeout, unit);

        this.logger.info("Waitable: await");

        try {
            if (!this.countDownLatch.await(timeout, unit))
                throw new RuntimeException("Timed out waiting for the observable to complete");
        } catch (final InterruptedException ie) {
            this.logger.catching(ie);

            Thread.currentThread().interrupt();

            throw new RuntimeException("Interrupted waiting for the observable to complete");
        }

        this.logger.exit(this);

        return this;
    }
}
