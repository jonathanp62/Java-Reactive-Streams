package net.jmp.demo.reactive.streams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

import java.util.stream.Stream;

// See: https://dzone.com/articles/mastering-own-reactive-streams-implementation-part

public final class Main {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    private Main() {
        super();
    }

    private void run() {
        this.logger.entry();

        /* A publisher of a stream of integers */

        new StreamPublisher<>(() -> Stream.of(1, 2, 3, 4, 5, 6))
                .subscribe(new Subscriber<>() {
                    @Override
                    public void onSubscribe(final Subscription subscription) {
                        logger.info("onSubscribe");

                        subscription.request(6);
                    }

                    @Override
                    public void onNext(final Integer item) {
                        logger.info("onNext: {}", item);
                    }

                    @Override
                    public void onError(final Throwable throwable) {
                        logger.error("onError: {}", throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        logger.info("onComplete");
                    }
                });

        this.logger.exit();
    }

    public static void main(final String[] args) {
        new Main().run();
    }
}
