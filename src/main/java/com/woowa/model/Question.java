package com.woowa.model;

import java.time.ZonedDateTime;

public class Question {
    private final String questionId;
    private String title;
    private String content;
    private final Author author;
    private final ZonedDateTime createdAt;

    private Question(String questionId, String title, String content, Author author, ZonedDateTime createdAt) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    public static Question create(String questionId, String title, String content, Author author, ZonedDateTime createdBy) {
        return new Question(questionId, title, content, author, createdBy);
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

    public Author getAuthor() {
        return author;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void checkAuthority(User user) {
        author.checkAuthority(user);
    }

    public void update(String title, String content) {
        if(title != null && !title.isBlank()) {
            this.title = title;
        }
        if(content != null && !content.isBlank()) {
            this.content = content;
        }
    }
}
