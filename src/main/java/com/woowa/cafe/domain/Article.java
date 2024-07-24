package com.woowa.cafe.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Article {

    private Long id;
    private String title;
    private String contents;
    private String writerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Article(final String title, final String contents, final String writerId) {
        this.id = null;
        this.title = title;
        this.contents = contents;
        this.writerId = writerId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getWriterId() {
        return writerId;
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(createdAt);
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
