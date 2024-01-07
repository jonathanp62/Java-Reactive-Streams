package net.jmp.demo.reactive.streams.rxjava;

/*
 * (#)PersonObserverTest.java   0.8.0   01/06/2024
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

public class PersonObserverTest {
    public PersonObserverTest() {
        super();
    }

    @Test
    public void testObserver() {
        final List<Person> people = List.of(
                new Person("Smith", "John", 55),
                new Person("Smith", "Jane", 52),
                new Person("Doe", "Larry", 32),
                new Person("Doe", "Lucy", 33),
                new Person("Ewing", "JR", 40),
                new Person("Ewing", "Sue Ellen", 40)
        );

        final List<String> expected = List.of(
                "John Smith",
                "Jane Smith",
                "Larry Doe",
                "Lucy Doe",
                "JR Ewing",
                "Sue Ellen Ewing"
        );

        final var observable = new PersonTransformingObservable(() -> people);
        final var personObserver = new PersonObserver();

        observable.create().subscribe(personObserver.create());

        await().atMost(1_000, TimeUnit.MILLISECONDS)
                .untilAsserted(
                        () -> assertThat(personObserver.getObservedPeople())
                                .containsAll(expected)
                );

        observable.destroy();
    }
}
