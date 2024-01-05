package net.jmp.demo.reactive.streams.rxjava;

/*
 * (#)ListObserver.java 0.8.0   01/05/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.8.0
 * @since     0.8.0
 */

import io.reactivex.rxjava3.annotations.NonNull;

import io.reactivex.rxjava3.core.Observer;

import io.reactivex.rxjava3.disposables.Disposable;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class ListObserver<T> extends WaitableObserver<T> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    public ListObserver() {
        super();
    }

    public Observer<T> create() {
        this.logger.entry();

        final var observer = new Observer<T>() {
            Disposable disposable;

            @Override
            public void onSubscribe(final @NonNull Disposable d) {
                logger.info("onSubscribe");

                this.disposable = d;
            }

            @Override
            public void onNext(final @NonNull Object o) {
                logger.info("onNext: {}", o);
            }

            @Override
            public void onError(final @NonNull Throwable e) {
                logger.info("onError: {}", e.getMessage());
            }

            @Override
            public void onComplete() {
                logger.info("onComplete");

                this.disposable.dispose();

                complete();
            }
        };

        this.logger.exit(observer);

        return observer;
    }

    private void complete() {
        this.logger.entry();

        super.onComplete();

        this.logger.exit();
    }
}
