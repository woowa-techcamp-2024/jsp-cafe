package org.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(articleId, article.articleId) && Objects.equals(title, article.title) && Objects.equals(content, article.content) && Objects.equals(author, article.author) && Objects.equals(createdDt, article.createdDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, title, content, author, createdDt);
    }
}
