package com.codesquad.cafe.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostDetailsDto {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Long postId;

    private String title;

    private String content;

    private String fileName;

    private Long authorId;

    private String authorUsername;

    private LocalDateTime createdAt;

    public PostDetailsDto(Long postId,
                          String title,
                          String content,
                          String fileName,
                          Long authorId,
                          String authorUsername,
                          LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
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
}
