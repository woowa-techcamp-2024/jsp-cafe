package org.example.reply.model.dto;

import java.time.LocalDateTime;
import org.example.reply.model.ReplyStatus;
import org.example.reply.model.dao.Reply;

public class ReplyDto {
    private Long id;
    private Long postId;
    private String userId;
    private String writer;
    private String contents;
    private ReplyStatus replyStatus;
    private LocalDateTime createdAt;

    private ReplyDto(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.postId = builder.postId;
        this.writer = builder.writer;
        this.contents = builder.contents;
        this.replyStatus = builder.replyStatus;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private Long id;
        private Long postId;
        private String userId;
        private String writer;
        private String contents;
        private ReplyStatus replyStatus;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder postId(Long postId) {
            this.postId = postId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder writer(String writer) {
            this.writer = writer;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder replyStatus(ReplyStatus replyStatus) {
            this.replyStatus = replyStatus;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReplyDto build() {
            return new ReplyDto(this);
        }
    }

    public static ReplyDto toDto(Reply reply) {
        return new Builder()
                .id(reply.getId())
                .postId(reply.getPostId())
                .userId(reply.getUserId())
                .writer(reply.getUserId())
                .contents(reply.getContents())
                .replyStatus(reply.getReplyStatus())
                .createdAt(reply.getCreatedAt())
                .build();
    }

    // Getter methods
    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public ReplyStatus getReplyStatus() {
        return replyStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "ReplyDto{" +
                "id=" + id +
                ", postId=" + postId +
                ", writer='" + writer + '\'' +
                ", contents='" + contents + '\'' +
                ", replyStatus=" + replyStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}