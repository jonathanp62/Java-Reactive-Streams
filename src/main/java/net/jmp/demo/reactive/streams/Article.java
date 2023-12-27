package net.jmp.demo.reactive.streams;

/*
 * (#)Article.java  0.3.0   12/27/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.3.0
 * @since     0.3.0
 */

final class Article {
    private final int id;
    private final String title;
    private final String category;

    Article(final int id, final String title, final String category) {
        this.id = id;
        this.title = title;
        this.category = category;
    }

    int getId() {
        return this.id;
    }

    String getTitle() {
        return this.title;
    }

    String getCategory() {
        return this.category;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + this.id +
                ", title='" + this.title + '\'' +
                ", category='" + this.category + '\'' +
                '}';
    }
}
