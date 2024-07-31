package org.example.demo.domain;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Long postId;
    private User writer;
    private String contents;
    private LocalDateTime createdAt;

    public Comment(Long id, Long postId, User writer, String contents, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.writer = writer;
        this.contents = contents;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
