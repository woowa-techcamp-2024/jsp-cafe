package com.woowa.model;

import com.woowa.exception.AuthorizationException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Reply {
    private final String replyId;
    private String content;
    private final Author author;
    private final QuestionInfo questionInfo;
    private final ZonedDateTime createdAt;
    private boolean deleted;

    private Reply(String replyId, String content, Author author, QuestionInfo questionInfo, ZonedDateTime createdAt, boolean deleted) {
        this.replyId = replyId;
        this.author = author;
        this.content = content;
        this.questionInfo = questionInfo;
        this.createdAt = createdAt;
        this.deleted = deleted;
    }

    public static Reply create(String replyId, String content, Author author, QuestionInfo questionInfo, ZonedDateTime createdAt) {
        return new Reply(replyId, content, author, questionInfo, createdAt, false);
    }

    public static Reply create(String replyId, String content, Author author, QuestionInfo questionInfo, ZonedDateTime createdAt, boolean deleted) {
        return new Reply(replyId, content, author, questionInfo, createdAt, deleted);
    }

    public void checkAuthority(User user) {
        author.checkAuthority(user);
    }

    public void checkAuthority(Author author) {
        if(this.author.equals(author)) {
            return;
        }
        throw new AuthorizationException("작성자가 다른 댓글은 삭제할 수 없습니다.");
    }

    public void delete() {
        deleted = true;
    }

    public String getReplyId() {
        return replyId;
    }

    public String getContent() {
        return content;
    }

    public Author getAuthor() {
        return author;
    }

    public QuestionInfo getQuestionInfo() {
        return questionInfo;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "replyId='" + replyId + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", questionInfo=" + questionInfo +
                ", createdAt=" + createdAt +
                ", delete=" + deleted +
                '}';
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
        return deleted == reply.deleted && Objects.equals(replyId, reply.replyId) && Objects.equals(
                content, reply.content) && Objects.equals(author, reply.author) && Objects.equals(
                questionInfo, reply.questionInfo) && Objects.equals(createdAt, reply.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyId, content, author, questionInfo, createdAt, deleted);
    }
}
