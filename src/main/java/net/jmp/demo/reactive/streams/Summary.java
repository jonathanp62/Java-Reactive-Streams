package net.jmp.demo.reactive.streams;

/*
 * (#)Summary.java  0.3.0   12/27/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.3.0
 * @since     0.3.0
 */

final class Summary {
    private final int id;
    private final String title;

    Summary(final int id, final String title) {
        this.id = id;
        this.title = title;
    }

    int getId() {
        return this.id;
    }

    String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
