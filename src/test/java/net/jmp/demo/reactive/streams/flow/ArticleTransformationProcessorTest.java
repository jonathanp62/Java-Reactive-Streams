package net.jmp.demo.reactive.streams.flow;

/*
 * (#)ArticleTransformationProcessorTest.java   0.6.0   01/01/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.6.0
 * @since     0.6.0
 */

import java.util.List;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

import static org.awaitility.Awaitility.await;

import static org.junit.Assert.*;

import org.junit.*;

public class ArticleTransformationProcessorTest {
    public ArticleTransformationProcessorTest() {
        super();
    }

    @Test
    public void testTransformation() {
        final List<Article> articles = List.of(
                new Article(1, "Java Functional Interface", "Java"),
                new Article(2, "Java Distinct Example", "Java"),
                new Article(3, "Java flatMap Example", "Java")
        );

        final List<Summary> summaries = List.of(
                new Summary(1, "Java Functional Interface"),
                new Summary(2, "Java Distinct Example"),
                new Summary(3, "Java flatMap Example")
        );

        final Function<Article, Summary> function = article ->
                new Summary(article.getId(), article.getTitle());

        final var processor = new ArticleTransformationProcessor(function);
        final var countDownLatch = new CountDownLatch(1);
        final var subscriber = new SummarySubscriber(countDownLatch);

        try (final SubmissionPublisher<Article> publisher = new SubmissionPublisher<>()) {
            publisher.subscribe(processor);     // Subscribe to transformer
            processor.subscribe(subscriber);    // Subscribe to summary

            articles.forEach(publisher::submit);    // article -> publisher.submit(article)
        }

        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(
                        () -> assertThat(subscriber.getConsumedSummaries())
                                .hasSize(3)
                );

        assertEquals(0, countDownLatch.getCount());

        assertTrue(subscriber.getConsumedSummaries().contains(summaries.get(0)));
        assertTrue(subscriber.getConsumedSummaries().contains(summaries.get(1)));
        assertTrue(subscriber.getConsumedSummaries().contains(summaries.get(2)));
    }
}
