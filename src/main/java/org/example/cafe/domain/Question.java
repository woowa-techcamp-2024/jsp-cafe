package org.example.cafe.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Question {

    private String questionId;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;

    public Question(String title,
                    String content,
                    String writer) {
        this.questionId = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = LocalDateTime.now();
    }

    public Question(String questionId,
                    String title,
                    String content,
                    String writer) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public String getQuestionId() {
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
