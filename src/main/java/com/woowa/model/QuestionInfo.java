package com.woowa.model;

import java.util.Objects;

public class QuestionInfo {
    private final String questionId;
    private final String content;

    public QuestionInfo(String questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }

    public static QuestionInfo from(Question question) {
        return new QuestionInfo(question.getQuestionId(), question.getContent());
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuestionInfo that = (QuestionInfo) o;
        return Objects.equals(questionId, that.questionId) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, content);
    }

    @Override
    public String toString() {
        return "QuestionInfo{" +
                "questionId='" + questionId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
