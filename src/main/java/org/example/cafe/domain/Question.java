package org.example.cafe.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Question {

    private Long questionId;
    private String title;
    private String content;
    private String writer;
    private boolean isDeleted;
    private LocalDateTime createdAt;

    public Question(String title,
                    String content,
                    String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public Question(Long questionId,
                    String title,
                    String content,
                    String writer) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public Question(Long questionId,
                    String title,
                    String content,
                    String writer,
                    boolean isDeleted,
                    LocalDateTime createdAt) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public boolean isValidWriter(String loginUserId) {
        return loginUserId != null && !loginUserId.equals(writer);
    }

    public void delete() {
        this.isDeleted = true;
    }

    // ----------------------------------------------------------------------------- Getter

    public Long getQuestionId() {
        return questionId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return isDeleted;
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
        Question question = (Question) o;
        return Objects.equals(questionId, question.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, title, content, writer);
    }
}
