package net.jmp.demo.reactive.streams.org;

/*
 * (#)StreamSubscriberBlackboxVerificationTest.java 0.4.0   12/28/2023
 * (#)StreamSubscriberBlackboxVerificationTest.java 0.2.0   12/26/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.1.0
 */

import org.reactivestreams.Subscriber;

import org.reactivestreams.tck.SubscriberBlackboxVerification;
import org.reactivestreams.tck.TestEnvironment;

public class StreamSubscriberBlackboxVerificationTest extends SubscriberBlackboxVerification<Integer> {
    public StreamSubscriberBlackboxVerificationTest() {
        super(new TestEnvironment());
    }

    @Override
    public Subscriber<Integer> createSubscriber() {
        return new StreamSubscriber<>();
    }

    @Override
    public Integer createElement(final int element) {
        return element;
    }
}
