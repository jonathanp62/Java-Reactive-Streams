package net.jmp.demo.reactive.streams;

/*
 * (#)ArticleTransformationProcessor.java 0.3.0   12/27/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.3.0
 * @since     0.3.0
 */

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;

import java.util.function.Function;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

final class ArticleTransformationProcessor extends SubmissionPublisher<Summary> implements Processor<Article, Summary> {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final Function<Article, Summary> function;

    private Subscription subscription;

    public ArticleTransformationProcessor(final Function<Article, Summary> function) {
        super();

        this.function = function;
    }

    @Override
    public void subscribe(final Flow.Subscriber<? super Summary> subscriber) {
        super.subscribe(subscriber);

        logger.info("subscribe");
    }

    @Override
    public void onSubscribe(final Subscription subscription) {
        logger.info("onSubscribe");

        if (this.subscription == null) {
            this.subscription = subscription;

            subscription.request(Long.MAX_VALUE);
        } else {
            this.subscription = null;

            subscription.cancel();
        }
    }

    @Override
    public void onNext(final Article article) {
        logger.info("onNext: {}", article);

        if (article == null)
            throw new NullPointerException("Null article received by onNext");

        submit(this.function.apply(article));
    }

    @Override
    public void onError(final Throwable throwable) {
        logger.error("onError: {}", throwable.getMessage());

    }

    @Override
    public void onComplete() {
        logger.info("onComplete");

        close();    // Close this processor (submission publisher)
    }
}
