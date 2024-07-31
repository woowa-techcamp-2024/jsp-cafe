package org.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import org.example.constance.AliveStatus;

public class Article {
    private Long articleId;
    private Long userId;
    private String title;
    private String content;
    private String author;
    private AliveStatus aliveStatus;
    private LocalDateTime createdDt;

    public Article(Long articleId, String title, String content, String author, LocalDateTime createdDt,
                   AliveStatus aliveStatus, Long userId) {
        this.articleId = articleId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.aliveStatus = aliveStatus;
        this.createdDt = createdDt;
    }

    public Article(String title, String content, String author, LocalDateTime createdDt, AliveStatus aliveStatus,
                   Long userId) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.aliveStatus = aliveStatus;
        this.createdDt = createdDt;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getUserId() {
        return userId;
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

    public AliveStatus getAlivestatus() {
        return aliveStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Article article = (Article) o;
        return Objects.equals(articleId, article.articleId) && Objects.equals(userId, article.userId)
                && Objects.equals(title, article.title) && Objects.equals(content, article.content)
                && Objects.equals(author, article.author) && aliveStatus == article.aliveStatus
                && Objects.equals(createdDt, article.createdDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, userId, title, content, author, aliveStatus, createdDt);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void delete() {
        this.aliveStatus = AliveStatus.DELETED;
    }
}
