package net.jmp.demo.reactive.streams.rxjava;

/*
 * (#)PersonTransformingObservable.java 0.8.0   01/07/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.8.0
 * @since     0.8.0
 */

import io.reactivex.rxjava3.core.Observable;

import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import java.util.function.Supplier;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public class PersonTransformingObservable {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final ExecutorService executor = ForkJoinPool.commonPool();
    private final Supplier<List<Person>> personSupplier;

    public PersonTransformingObservable(final Supplier<List<Person>> personSupplier) {
        super();

        this.personSupplier = personSupplier;
    }

    public Observable<String> create() {
        this.logger.entry();

        // Transform a person object into a string object

        final var observable = Observable
                .fromIterable(this.personSupplier.get())
                .map(person -> person.firstName() + " " + person.lastName())
                .sorted()
                .subscribeOn(Schedulers.from(this.executor));

        this.logger.exit(observable);

        return observable;
    }

    public void destroy() {
        this.logger.entry();

        if (!this.executor.isShutdown())
            this.executor.shutdown();

        this.logger.exit();
    }
}
