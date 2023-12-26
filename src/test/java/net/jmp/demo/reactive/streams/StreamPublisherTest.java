package net.jmp.demo.reactive.streams;

/*
 * (#)StreamPublisherTest.java  0.2.0   12/25/2023
 * (#)StreamPublisherTest.java  0.1.0   12/25/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.2.0
 * @since     0.1.0
 */

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

import java.util.stream.Stream;

public class StreamPublisherTest extends PublisherVerification<Integer> {
    public StreamPublisherTest() {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<Integer> createPublisher(final long elements) {
        return new StreamPublisher<>(() -> Stream.of(1, 2, 3, 4, 5));
    }

    @Override
    public Publisher<Integer> createFailedPublisher() {
        return new Publisher<>() {
            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                s.onSubscribe(null);
                s.onError(new RuntimeException("Can't subscribe subscriber: " + s + ", because of reasons."));
            }
        };
    }

    @Override
    public long maxElementsFromPublisher() {
        return Long.MAX_VALUE;
    }

    @Override
    public long boundedDepthOfOnNextAndRequestRecursion() {
        return 1;
    }
}
