package net.jmp.demo.reactive.streams;

/*
 * (#)Main.java 0.4.0   12/28/2023
 * (#)Main.java 0.3.0   12/27/2023
 * (#)Main.java 0.2.0   12/25/2023
 * (#)Main.java 0.1.0   12/25/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.1.0
 */

import java.util.Arrays;
import java.util.List;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import java.util.function.Function;

import java.util.stream.Stream;

import net.jmp.demo.reactive.streams.flow.*;
import net.jmp.demo.reactive.streams.org.*;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

// See: https://dzone.com/articles/mastering-own-reactive-streams-implementation-part

public final class Main {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    private Main() {
        super();
    }

    private void run() {
        this.logger.entry();

        this.publishAndSubscribeWithOrg();
        this.publishAndSubscribeWithFlow();

        this.transform();

        this.logger.exit();
    }

    private void publishAndSubscribeWithOrg() {
        this.logger.entry();

        /* A publisher and subscriber of a stream of integers */

        new StreamPublisher<>(() -> Stream.of(1, 2, 3, 4, 5, 6))
                .subscribe(new StreamSubscriber<>());

        this.logger.exit();
    }

    private void publishAndSubscribeWithFlow() {
        this.logger.entry();

        new ListPublisher<>(() -> List.of("Red",
                "Orange",
                "Yellow",
                "Green",
                "Blue",
                "Indigo",
                "Violet")
        ).subscribe(new ListSubscriber<>());

        this.logger.exit();
    }

    private void transform() {
        this.logger.entry();

        final List<Article> list = Arrays.asList(
                new Article(1, "Java Functional Interface", "Java"),
                new Article(2, "Java Distinct Example", "Java"),
                new Article(3, "Java flatMap Example", "Java")
        );

        final Function<Article, Summary> function = article ->
                new Summary(article.getId(), article.getTitle());

        final var processor = new ArticleTransformationProcessor(function);
        final var countDownLatch = new CountDownLatch(1);

        try (final SubmissionPublisher<Article> publisher = new SubmissionPublisher<>()) {
            final SummarySubscriber subscriber = new SummarySubscriber(countDownLatch);

            publisher.subscribe(processor);     // Subscribe to transformer
            processor.subscribe(subscriber);    // Subscribe to summary

            list.forEach(publisher::submit);    // article -> publisher.submit(article)
        }

        try {
            if (!countDownLatch.await(2, TimeUnit.SECONDS))
                this.logger.error("Timeout waiting for the summary subscriber to complete");
        } catch (final InterruptedException ie) {
            this.logger.catching(ie);

            Thread.currentThread().interrupt();
        }

        this.logger.exit();
    }

    public static void main(final String[] args) {
        new Main().run();
    }
}
