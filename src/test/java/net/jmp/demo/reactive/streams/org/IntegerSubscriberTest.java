package net.jmp.demo.reactive.streams.org;

/*
 * (#)IntegerSubscriberTest.java    0.7.0   01/03/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.7.0
 * @since     0.7.0
 */

import java.util.List;

import java.util.concurrent.TimeUnit;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.awaitility.Awaitility.await;

import static org.junit.Assert.*;

import org.junit.*;

public class IntegerSubscriberTest {
    public IntegerSubscriberTest() {
        super();
    }

    @Test
    public void testSubscriber() {
        final var items = List.of(
                1, 2, 3, 4, 5, 6
        );

        final var subscriber = new IntegerSubscriber();

        try (final var publisher = new StreamPublisher<>(() -> Stream.of(1, 2, 3, 4, 5, 6))) {
            publisher.subscribe(subscriber);

            assertTrue(publisher.isSubscribed());
        }

        await().atMost(1_000, TimeUnit.MILLISECONDS)
                .untilAsserted(
                        () -> assertThat(subscriber.getConsumedIntegers())
                                .containsAll(items)
                );

        assertEquals(6, subscriber.getConsumedIntegers().size());
    }
}
