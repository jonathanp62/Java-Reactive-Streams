package net.jmp.demo.reactive.streams.rxjava;

/*
 * (#)Person.java   0.8.0   01/07/2024
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.8.0
 * @since     0.8.0
 */

public record Person(
        String lastName,
        String firstName,
        int age
) {
}
