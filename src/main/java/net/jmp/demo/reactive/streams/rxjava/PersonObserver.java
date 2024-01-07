package net.jmp.demo.reactive.streams.rxjava;

/*
 * (#)PersonObserver.java   0.8.0   01/07/2024
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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class PersonObserver extends WaitableObserver<String> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final List<String> observedPeople = new ArrayList<>();

    public PersonObserver() {
        super();
    }

    public Observer<String> create() {
        this.logger.entry();

        final var observer = new Observer<String>() {
            Disposable disposable;

            @Override
            public void onSubscribe(final @NonNull Disposable d) {
                logger.info("onSubscribe");

                this.disposable = d;
            }

            @Override
            public void onNext(final @NonNull String personName) {
                logger.info("onNext: {}", personName);

                observedPeople.add(personName);
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

    public List<String> getObservedPeople() {
        return this.observedPeople;
    }

    private void complete() {
        this.logger.entry();

        super.onComplete();

        this.logger.exit();
    }
}
