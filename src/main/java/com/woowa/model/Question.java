package com.woowa.model;

public class Question {
    private final String questionId;
    private final String title;
    private final String content;
    private final Author author;

    private Question(String questionId, String title, String content, Author author) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static Question create(String questionId, String title, String content, Author author) {
        return new Question(questionId, title, content, author);
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
}
