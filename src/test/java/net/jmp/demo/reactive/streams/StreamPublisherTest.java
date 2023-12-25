package net.jmp.demo.reactive.streams;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

import java.util.stream.Stream;

public class StreamPublisherTest extends PublisherVerification<Integer> {
    public StreamPublisherTest(final TestEnvironment env, final long publisherReferenceGCTimeoutMillis) {
        super(env, publisherReferenceGCTimeoutMillis);
    }

    public StreamPublisherTest(final TestEnvironment env) {
        super(env);
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
                s.onError(new RuntimeException("Can't subscribe subscriber: " + s + ", because of reasons."));
            }
        };
    }

    @Override
    public long maxElementsFromPublisher() {
        return Long.MAX_VALUE - 1;
    }

    @Override
    public long boundedDepthOfOnNextAndRequestRecursion() {
        return 1;
    }
}
