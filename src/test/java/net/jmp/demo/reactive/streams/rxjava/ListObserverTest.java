package net.jmp.demo.reactive.streams.rxjava;

/*
 * (#)ListObserverTest.java   0.8.0   01/06/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.6.0
 * @since     0.6.0
 */

import java.util.List;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import static org.awaitility.Awaitility.await;

import org.junit.*;

public class ListObserverTest {
    public ListObserverTest() {
        super();
    }

    @Test
    public void testObserver() {
        final var items = List.of(
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
        );

        final var observable = new ListObservable<>(() -> items);
        final var source = observable.create();
        final var listObserver = new ListObserver<String>();
        final var observer = listObserver.create();

        source.subscribe(observer);

        await().atMost(1_000, TimeUnit.MILLISECONDS)
                .untilAsserted(
                        () -> assertThat(listObserver.getObservedElements())
                                .containsAll(items)
                );

        observable.destroy();
    }
}
