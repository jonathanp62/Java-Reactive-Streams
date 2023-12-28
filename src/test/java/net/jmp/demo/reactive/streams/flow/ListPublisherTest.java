package net.jmp.demo.reactive.streams.flow;

/*
 * (#)ListPublisherTest.java    0.4.0   12/28/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.4.0
 */

import java.util.List;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

import org.reactivestreams.tck.TestEnvironment;

import org.reactivestreams.tck.flow.FlowPublisherVerification;

public class ListPublisherTest extends FlowPublisherVerification<String> {
    public ListPublisherTest() {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<String> createFlowPublisher(final long elements) {
        return new ListPublisher<>(() -> List.of("Red",
                "Orange",
                "Yellow",
                "Green",
                "Blue",
                "Indigo",
                "Violet"));
    }

    @Override
    public Publisher<String> createFailedFlowPublisher() {
        return new Publisher<>() {
            @Override
            public void subscribe(Subscriber<? super String> s) {
                s.onSubscribe(null);
                s.onError(new RuntimeException("Can't subscribe subscriber: " + s + ", because of reasons."));
            }
        };
    }

    public long maxElementsFromPublisher() {
        return Long.MAX_VALUE;
    }

    @Override
    public long boundedDepthOfOnNextAndRequestRecursion() {
        return 1;
    }
}
