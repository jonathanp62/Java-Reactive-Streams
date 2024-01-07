package net.jmp.demo.reactive.streams;

/*
 * (#)Main.java 0.8.0   01/05/2024
 * (#)Main.java 0.7.0   01/03/2024
 * (#)Main.java 0.5.0   12/28/2023
 * (#)Main.java 0.4.0   12/28/2023
 * (#)Main.java 0.3.0   12/27/2023
 * (#)Main.java 0.2.0   12/25/2023
 * (#)Main.java 0.1.0   12/25/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.8.0
 * @since     0.1.0
 */

import java.util.Arrays;
import java.util.List;

import java.util.concurrent.SubmissionPublisher;

import java.util.function.Function;

import java.util.stream.Stream;

import net.jmp.demo.reactive.streams.flow.*;
import net.jmp.demo.reactive.streams.org.*;
import net.jmp.demo.reactive.streams.rxjava.*;

import org.reactivestreams.FlowAdapters;

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
        this.publishAndSubscribeWithRxJava();

        this.transformWithOrg();
        this.transformWithFlow();
        this.transformWithRxJava();

        this.logger.exit();
    }

    private void publishAndSubscribeWithOrg() {
        this.logger.entry();

        /* A publisher and subscriber of a stream of integers */

        final var subscriber = new StreamSubscriber<>();

        try (final var publisher = new StreamPublisher<>(() -> Stream.of(1, 2, 3, 4, 5, 6))) {
            publisher.subscribe(subscriber);
        }

        subscriber.await();

        this.logger.info("Consumed: {}", subscriber.getConsumedElements());

        this.logger.exit();
    }

    private void publishAndSubscribeWithFlow() {
        this.logger.entry();

        final var subscriber = new ListSubscriber<String>();

        try (final var publisher = new ListPublisher<>(() -> List.of("Red",
                "Orange",
                "Yellow",
                "Green",
                "Blue",
                "Indigo",
                "Violet")
        )) {
            publisher.subscribe(subscriber);
        }

        subscriber.await();

        this.logger.info("Consumed: {}", subscriber.getConsumedElements());

        this.logger.exit();
    }

    private void publishAndSubscribeWithRxJava() {
        this.logger.entry();

        final var observable = new ListObservable<>(() -> List.of(
                "Bach",
                "Beethoven",
                "Brahms",
                "Bruckner",
                "Cavalli",
                "Haydn",
                "Mozart",
                "Puccini",
                "Purcell",
                "Scarlatti",
                "Verdi",
                "Wagner"
        ));

        final var source = observable.create();
        final var listObserver = new ListObserver<String>();
        final var observer = listObserver.create();

        source.subscribe(observer);

        listObserver.await();

        this.logger.info("Observed: {}", listObserver.getObservedElements());

        observable.destroy();

        this.logger.exit();
    }

    private void transformWithOrg() {
        this.logger.entry();

        final var subscriber = new IntegerSubscriber();

        try (final var publisher = new SubmissionPublisher<Integer>()) {
            final var processor = new DoublingProcessor();

            publisher.subscribe(FlowAdapters.toFlowProcessor(processor));
            processor.subscribe(subscriber);

            publisher.submit(1);
            publisher.submit(2);
            publisher.submit(3);
            publisher.submit(4);
        }

        subscriber.await();

        this.logger.info("Consumed: {}", subscriber.getConsumedIntegers());

        this.logger.exit();
    }

    private void transformWithFlow() {
        this.logger.entry();

        final List<Article> list = Arrays.asList(
                new Article(1, "Java Functional Interface", "Java"),
                new Article(2, "Java Distinct Example", "Java"),
                new Article(3, "Java flatMap Example", "Java")
        );

        final Function<Article, Summary> function = article ->
                new Summary(article.getId(), article.getTitle());

        final var processor = new ArticleTransformationProcessor(function);
        final var subscriber = new SummarySubscriber();

        try (final SubmissionPublisher<Article> publisher = new SubmissionPublisher<>()) {
            publisher.subscribe(processor);     // Subscribe to transformer
            processor.subscribe(subscriber);    // Subscribe to summary

            list.forEach(publisher::submit);    // article -> publisher.submit(article)
        }

        subscriber.await();

        this.logger.info("Consumed: {}", subscriber.getConsumedSummaries());

        this.logger.exit();
    }

    private void transformWithRxJava() {
        this.logger.entry();

        final List<Person> people = List.of(
                new Person("Smith", "John", 55),
                new Person("Smith", "Jane", 52),
                new Person("Doe", "Larry", 32),
                new Person("Doe", "Lucy", 33),
                new Person("Ewing", "JR", 40),
                new Person("Ewing", "Sue Ellen", 40)
        );

        final var observable = new PersonTransformingObservable(() -> people);
        final var personObserver = new PersonObserver();

        observable.create().subscribe(personObserver.create());

        personObserver.await();

        this.logger.info("Observed: {}", personObserver.getObservedPeople());

        observable.destroy();

        this.logger.exit();
    }

    public static void main(final String[] args) {
        new Main().run();
    }
}
