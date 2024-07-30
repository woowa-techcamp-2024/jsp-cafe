package com.woowa.model;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Reply {
    private final String replyId;
    private String content;
    private final Author author;
    private final QuestionInfo questionInfo;
    private final ZonedDateTime createdAt;
    private boolean delete;

    private Reply(String replyId, String content, Author author, QuestionInfo questionInfo, ZonedDateTime createdAt) {
        this.replyId = replyId;
        this.author = author;
        this.content = content;
        this.questionInfo = questionInfo;
        this.createdAt = createdAt;
    }

    public static Reply create(String replyId, String content, Author author, QuestionInfo questionInfo, ZonedDateTime createdAt) {
        return new Reply(replyId, content, author, questionInfo, createdAt);
    }

    public void checkAuthority(User user) {
        author.checkAuthority(user);
    }

    public void delete() {
        delete = true;
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

    public boolean isDelete() {
        return delete;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "replyId='" + replyId + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", questionInfo=" + questionInfo +
                ", createdAt=" + createdAt +
                ", delete=" + delete +
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
        return delete == reply.delete && Objects.equals(replyId, reply.replyId) && Objects.equals(
                content, reply.content) && Objects.equals(author, reply.author) && Objects.equals(
                questionInfo, reply.questionInfo) && Objects.equals(createdAt, reply.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(replyId, content, author, questionInfo, createdAt, delete);
    }
}
