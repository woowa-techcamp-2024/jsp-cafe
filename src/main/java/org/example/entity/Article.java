package org.example.entity;

public class Article {

    private Integer articleId;
    private final String title;
    private final String content;
    private final String author;

    public Article(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getId() {
        return articleId;
    }

    public void setId(Integer id) {
        this.articleId = id;
    }

    public String getAuthor() {
        return author;
    }
}
