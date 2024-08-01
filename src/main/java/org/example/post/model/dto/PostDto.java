package org.example.post.model.dto;

import java.time.LocalDateTime;
import org.example.post.model.PostStatus;

public class PostDto {
    private Long id;
    private String userId;
    private String username;
    private String title;
    private String contents;
    private PostStatus status;
    private LocalDateTime createdAt;

    private PostDto(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.username = builder.username;
        this.title = builder.title;
        this.contents = builder.contents;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private Long id;
        private String userId;
        private String username;
        private String title;
        private String contents;
        private PostStatus status;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder status(PostStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PostDto build() {
            return new PostDto(this);
        }
    }


    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public PostStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PostDto updatePost(String title, String contents) {
        this.title = title;
        this.contents = contents;
        return this;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}