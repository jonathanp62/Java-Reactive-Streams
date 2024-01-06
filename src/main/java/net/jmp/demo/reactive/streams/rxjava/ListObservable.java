package net.jmp.demo.reactive.streams.rxjava;

/*
 * (#)ListObservable.java   0.8.0   01/05/2024
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

public class ListObservable<T> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final ExecutorService executor = ForkJoinPool.commonPool();
    private final Supplier<List<T>> listSupplier;

    public ListObservable(final Supplier<List<T>> listSupplier) {
        super();

        this.listSupplier = listSupplier;
    }

    public Observable<T> create() {
        this.logger.entry();

        final var observable = Observable
                .fromIterable(this.listSupplier.get())
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
