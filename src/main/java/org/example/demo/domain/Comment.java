package org.example.demo.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {
    private Long id;
    private Long postId;
    private User writer;
    private String contents;
    private LocalDateTime createdAt;

    public Comment() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(postId, comment.postId) && Objects.equals(writer, comment.writer) && Objects.equals(contents, comment.contents) && Objects.equals(createdAt, comment.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, writer, contents, createdAt);
    }
}
