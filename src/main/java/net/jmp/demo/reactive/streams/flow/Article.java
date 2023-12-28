package net.jmp.demo.reactive.streams.flow;

/*
 * (#)Article.java  0.4.0   12/28/2023
 * (#)Article.java  0.3.0   12/27/2023
 *
 * Copyright (c) Jonathan M. Parker
 * All Rights Reserved.
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.3.0
 */

public final class Article {
    private final int id;
    private final String title;
    private final String category;

    public Article(final int id, final String title, final String category) {
        this.id = id;
        this.title = title;
        this.category = category;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCategory() {
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
