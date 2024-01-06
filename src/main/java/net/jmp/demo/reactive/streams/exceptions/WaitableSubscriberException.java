package net.jmp.demo.reactive.streams.exceptions;

/*
 * (#)WaitableSubscriberException.java  0.8.0   01/06/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.8.0
 * @since     0.8.0
 */

public class WaitableSubscriberException extends RuntimeException {
    public WaitableSubscriberException() {
        super();
    }

    public WaitableSubscriberException(final String message) {
        super(message);
    }

    public WaitableSubscriberException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public WaitableSubscriberException(final Throwable throwable) {
        super(throwable);
    }
}
