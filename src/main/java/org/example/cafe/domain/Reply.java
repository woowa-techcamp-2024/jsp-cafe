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

    public Reply(String writer,
                 String content,
                 Long questionId) {
        this.writer = writer;
        this.content = content;
        this.questionId = questionId;
    }

    public Reply(Long replyId,
                 String writer,
                 String content,
                 Boolean isDeleted,
                 Long questionId,
                 LocalDateTime createdAt) {
        this.replyId = replyId;
        this.writer = writer;
        this.content = content;
        this.isDeleted = isDeleted;
        this.questionId = questionId;
        this.createdAt = createdAt;
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
