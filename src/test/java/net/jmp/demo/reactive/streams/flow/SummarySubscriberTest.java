package net.jmp.demo.reactive.streams.flow;

/*
 * (#)SummarySubscriberTest.java    0.7.0   01/04/2024
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

import static org.assertj.core.api.Assertions.assertThat;

import static org.awaitility.Awaitility.await;

import static org.junit.Assert.*;

import org.junit.*;

public class SummarySubscriberTest {
    public SummarySubscriberTest() {
        super();
    }

    @Test
    public void testSubscriber() {
        final var summaries = List.of(
                new Summary(10, "The Far Side"),
                new Summary(25, "Missa Solemnis"),
                new Summary(40, "Gramophone Magazine")
        );

        final var subscriber = new SummarySubscriber();

        try (final var publisher = new SummaryPublisher(() -> summaries)) {
            publisher.subscribe(subscriber);

            assertTrue(publisher.isSubscribed());
        }

        await().atMost(1_000, TimeUnit.MILLISECONDS)
                .untilAsserted(
                        () -> assertThat(subscriber.getConsumedSummaries())
                                .containsAll(summaries)
                );
    }
}
