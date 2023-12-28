package net.jmp.demo.reactive.streams.flow;

/*
 * (#)ListSubscriberBlackboxVerificationTest.java   0.4.0   12/28/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.4.0
 */

import java.util.concurrent.Flow.Subscriber;

import org.reactivestreams.tck.TestEnvironment;

import org.reactivestreams.tck.flow.FlowSubscriberBlackboxVerification;

public class ListSubscriberBlackboxVerificationTest extends FlowSubscriberBlackboxVerification<Integer> {
    public ListSubscriberBlackboxVerificationTest() {
        super(new TestEnvironment());
    }

    @Override
    public Integer createElement(final int element) {
        return element;
    }

    @Override
    public Subscriber<Integer> createFlowSubscriber() {
        return new ListSubscriber<>();
    }
}
