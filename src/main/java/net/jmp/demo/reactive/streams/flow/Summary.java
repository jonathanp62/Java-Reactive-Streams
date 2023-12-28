package net.jmp.demo.reactive.streams.flow;

/*
 * (#)Summary.java  0.4.0   12/28/2023
 * (#)Summary.java  0.3.0   12/27/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.3.0
 */

public final class Summary {
    private final int id;
    private final String title;

    public Summary(final int id, final String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
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
