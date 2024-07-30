package org.example.cafe.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reply {

    private Long replyId;
    private String writer;
    private String content;
    private Boolean isDeleted;
    private Long questionId;
    private LocalDateTime createdAt;

    private Reply(ReplyBuilder builder) {
        this.replyId = builder.replyId;
        this.writer = builder.writer;
        this.content = builder.content;
        this.isDeleted = builder.isDeleted;
        this.questionId = builder.questionId;
        this.createdAt = builder.createdAt;
    }

    public boolean isValidWriter(String loginUserId) {
        return loginUserId != null && !loginUserId.equals(writer);
    }

    public void delete() {
        this.isDeleted = true;
    }

    // ------------------------------------------------------------------------ Builder

    public static class ReplyBuilder {
        private Long replyId;
        private String writer;
        private String content;
        private Boolean isDeleted = false; // Default value
        private Long questionId;
        private LocalDateTime createdAt = LocalDateTime.now(); // Default value

        public ReplyBuilder replyId(Long replyId) {
            this.replyId = replyId;
            return this;
        }

        public ReplyBuilder writer(String writer) {
            this.writer = writer;
            return this;
        }

        public ReplyBuilder content(String content) {
            this.content = content;
            return this;
        }

        public ReplyBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public ReplyBuilder questionId(Long questionId) {
            this.questionId = questionId;
            return this;
        }

        public ReplyBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Reply build() {
            return new Reply(this);
        }
    }

    //------------------------------------------------------------------------- Getter

    public Long getReplyId() {
        return replyId;
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reply reply = (Reply) o;
        return Objects.equals(replyId, reply.replyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyId);
    }
}
