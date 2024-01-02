package net.jmp.demo.reactive.streams.org;

/*
 * (#)DoublingProcessorTest.java    0.6.0   01/02/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.6.0
 * @since     0.6.0
 */

import java.util.List;

import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import static org.awaitility.Awaitility.await;

import static org.junit.Assert.*;

import org.junit.*;
import org.reactivestreams.FlowAdapters;

public class DoublingProcessorTest {
    public DoublingProcessorTest() {
        super();
    }

    @Test
    public void testTransformation() {
        final var expected = List.of(2, 4, 6, 8);
        final var subscriber = new IntegerSubscriber();

        try (final var publisher = new SubmissionPublisher<Integer>()) {
            final var processor = new DoublingProcessor();

            publisher.subscribe(FlowAdapters.toFlowProcessor(processor));
            processor.subscribe(subscriber);

            publisher.submit(1);
            publisher.submit(2);
            publisher.submit(3);
            publisher.submit(4);
        }

        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(
                        () -> assertThat(subscriber.getConsumedIntegers())
                                .hasSize(4)
                );

        assertArrayEquals(expected.toArray(), subscriber.getConsumedIntegers().toArray(new Integer[0]));
    }
}
