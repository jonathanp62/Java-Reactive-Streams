package net.jmp.demo.reactive.streams.exceptions;

/*
 * (#)WaitableObserverException.java    0.8.0   01/06/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.8.0
 * @since     0.8.0
 */

public class WaitableObserverException extends RuntimeException {
    public WaitableObserverException() {
        super();
    }

    public WaitableObserverException(final String message) {
        super(message);
    }

    public WaitableObserverException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public WaitableObserverException(final Throwable throwable) {
        super(throwable);
    }
}
