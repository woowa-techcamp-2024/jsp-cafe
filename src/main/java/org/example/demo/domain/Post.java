package org.example.demo.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Post {
    private Long id;
    private User writer;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private List<Comment> comments;

    public Post(Long id, User writer, String title, String contents, LocalDateTime createdAt, List<Comment> comments) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", writer=" + writer +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                ", comments=" + comments +
                '}';
    }
}
