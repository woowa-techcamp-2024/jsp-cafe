package com.codesquad.cafe.db.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostDetailsDto {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Long postId;

    private String title;

    private String content;

    private String fileName;

    private int view;

    private Long authorId;

    private String authorUsername;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deleted;

    public PostDetailsDto(Long postId,
                          String title,
                          String content,
                          String fileName,
                          int view,
                          Long authorId,
                          String authorUsername,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt,
                          boolean deleted) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.view = view;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    public PostDetailsDto(Post post, User user) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.fileName = post.getFileName();
        this.authorId = post.getAuthorId();
        this.authorUsername = user.getUsername();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.deleted = post.isDeleted();
    }

    public String getFormattedDate() {
        return createdAt.format(formatter);
    }

    public Long getPostId() {
        return postId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
