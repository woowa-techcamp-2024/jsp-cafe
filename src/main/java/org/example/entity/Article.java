package org.example.entity;

public class Article {

    private Integer articleId;
    private final String title;
    private final String content;
    private final String author;

    public Article(Integer articleId, String title, String content, String author) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.author = author;
    }

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

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer id) {
        this.articleId = id;
    }

    public String getAuthor() {
        return author;
    }
}
