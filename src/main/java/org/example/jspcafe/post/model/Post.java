package org.example.jspcafe.post.model;

import org.example.jspcafe.PK;

import java.time.LocalDateTime;

public class Post {

    @PK
    private Long postId;
    private final Long userId;
    private Title title;
    private Content content;
    private LocalDateTime createdAt;

    public boolean canModifyBy(Long userId) {
        return this.userId.equals(userId);
    }

    public void updateTitle(String title) {
        this.title = new Title(title);
    }

    public void updateContent(String content) {
        this.content = new Content(content);
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public Title getTitle() {
        return title;
    }

    public Content getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Post(
            Long userId,
            String title,
            String content,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.title = new Title(title);
        this.content = new Content(content);
        validateCreatedAt(createdAt);
        this.createdAt = createdAt;
    }

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("생성일이 없습니다.");
        }
    }
}
