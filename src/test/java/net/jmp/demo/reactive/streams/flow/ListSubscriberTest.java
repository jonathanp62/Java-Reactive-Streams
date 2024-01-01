package net.jmp.demo.reactive.streams.flow;

/*
 * (#)ListSubscriberTest.java   0.6.0   01/01/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.6.0
 * @since     0.6.0
 */

import java.util.List;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import static org.awaitility.Awaitility.await;

import static org.junit.Assert.*;

import org.junit.*;

public class ListSubscriberTest {
    public ListSubscriberTest() {
        super();
    }

    @Test
    public void testSubscriber() throws InterruptedException {
        final var items = List.of(
                "Red",
                "Orange",
                "Yellow",
                "Green",
                "Blue",
                "Indigo",
                "Violet"
        );

        final var subscriber = new ListSubscriber<String>();

        try (final var publisher = new ListPublisher<>(() -> items)) {
            publisher.subscribe(subscriber);

            assertTrue(publisher.isSubscribed());
        }

        await().atMost(1_000, TimeUnit.MILLISECONDS)
                .untilAsserted(
                        () -> assertThat(subscriber.getConsumedElements())
                                .containsAll(items)
                );
    }
}
