package net.jmp.demo.reactive.streams;

/*
 * (#)StreamSubscriberWhiteboxVerificationTest.java 0.2.0   12/26/2023
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

import org.reactivestreams.tck.SubscriberWhiteboxVerification;
import org.reactivestreams.tck.TestEnvironment;

public class StreamSubscriberWhiteboxVerificationTest extends SubscriberWhiteboxVerification<Integer> {
    public StreamSubscriberWhiteboxVerificationTest() {
        super(new TestEnvironment());
    }

    @Override
    public Subscriber<Integer> createSubscriber(WhiteboxSubscriberProbe<Integer> probe) {
        return new StreamSubscriber<>() {
            @Override
            public void onSubscribe(final Subscription s) {
                super.onSubscribe(s);

                // Register a successful Subscription, and create a Puppet,
                // for the WhiteboxVerification to be able to drive its tests:

                probe.registerOnSubscribe(new SubscriberPuppet() {
                    @Override
                    public void triggerRequest(long elements) {
                        s.request(elements);
                    }

                    @Override
                    public void signalCancel() {
                        s.cancel();
                    }
                });
            }

            @Override
            public void onNext(Integer element) {
                // In addition to normal Subscriber work that you're testing, register onNext with the probe
                super.onNext(element);

                probe.registerOnNext(element);
            }

            @Override
            public void onError(Throwable cause) {
                // In addition to normal Subscriber work that you're testing, register onError with the probe
                super.onError(cause);

                probe.registerOnError(cause);
            }

            @Override
            public void onComplete() {
                // In addition to normal Subscriber work that you're testing, register onComplete with the probe
                super.onComplete();

                probe.registerOnComplete();
            }
        };
    }

    @Override
    public Integer createElement(final int element) {
        return element;
    }
}
