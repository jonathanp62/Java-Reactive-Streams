package net.jmp.demo.reactive.streams;

/*
 * (#)Main.java 0.2.0   12/25/2023
 * (#)Main.java 0.1.0   12/25/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.2.0
 * @since     0.1.0
 */

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

import java.util.stream.Stream;

// See: https://dzone.com/articles/mastering-own-reactive-streams-implementation-part

public final class Main {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    private Main() {
        super();
    }

    private void run() {
        this.logger.entry();

        /* A publisher and subscriber of a stream of integers */

        new StreamPublisher<>(() -> Stream.of(1, 2, 3, 4, 5, 6))
                .subscribe(new StreamSubscriber<>());

        this.logger.exit();
    }

    public static void main(final String[] args) {
        new Main().run();
    }
}
