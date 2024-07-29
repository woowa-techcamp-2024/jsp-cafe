package com.codesquad.cafe.db.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private Long id;

    private Long authorId;

    private String title;

    private String content;

    private String fileName;

    private Integer view;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deleted;

    public Post() {
    }

    public Post(Long id, Long authorId, String title, String content, String fileName, Integer view,
                LocalDateTime createdAt, LocalDateTime updatedAt, Boolean deleted) {
        if (authorId == null || authorId < 1) {
            throw new IllegalArgumentException("authorId는 1 이상의 값이어야 합니다.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title 필수 값입니다.");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("content 필수 값입니다.");
        }
        if (view < 0) {
            throw new IllegalArgumentException("view 는 0이상이어야 합니다.");
        }
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.view = view;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    public static Post of(Long authorId, String title, String content, String fileName) {
        LocalDateTime now = LocalDateTime.now();
        return new Post(null, authorId, title, content, fileName, 0, now, now, false);
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFileName() {
        return fileName;
    }

    public int getView() {
        return view;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void update(String content, String title, String fileName) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("content is required");
        }
        this.content = content;
        this.title = title;
        this.fileName = fileName;
        this.updatedAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void addView() {
        this.view++;
    }

    public void delete() {
        this.deleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Post{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(authorId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", filename='").append(fileName).append('\'');
        sb.append(", view='").append(view).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(deleted ? ", deleted" : "");
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(authorId, post.authorId)
                && Objects.equals(title, post.title) && Objects.equals(content, post.content)
                && Objects.equals(fileName, post.fileName) && Objects.equals(view, post.view)
                && Objects.equals(createdAt, post.createdAt) && Objects.equals(updatedAt,
                post.updatedAt) && Objects.equals(deleted, post.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorId, title, content, fileName, view, createdAt, updatedAt, deleted);
    }
}