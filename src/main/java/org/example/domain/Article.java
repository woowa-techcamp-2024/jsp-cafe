package org.example.domain;

import java.time.LocalDateTime;

public class Article {
    private Long articleId;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDt;

    public Article(String title, String content, String author, LocalDateTime createdDt) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDt = createdDt;
    }

    public Article(Long articleId, String title, String content, String author, LocalDateTime createdDt) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDt = createdDt;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }
}
