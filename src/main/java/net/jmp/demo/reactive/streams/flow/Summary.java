package net.jmp.demo.reactive.streams.flow;

/*
 * (#)Summary.java  0.6.0   01/01/2024
 * (#)Summary.java  0.4.0   12/28/2023
 * (#)Summary.java  0.3.0   12/27/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.6.0
 * @since     0.3.0
 */

import java.util.Objects;

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
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Summary summary = (Summary) o;

        return this.id == summary.id && Objects.equals(this.title, summary.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title);
    }

    @Override
    public String toString() {
        return "Summary{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
