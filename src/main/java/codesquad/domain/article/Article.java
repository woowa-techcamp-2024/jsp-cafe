package codesquad.domain.article;

import codesquad.exception.UnauthorizedRequestException;

public class Article {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private Status status;

    public Article(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.status = Status.PUBLISHED;
    }

    public Article(Long id, String title, String writer, String content) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.status = Status.PUBLISHED;
    }

    public Article(Long id, Article article) {
        this.id = id;
        this.title = article.title;
        this.writer = article.writer;
        this.content = article.content;
        this.status = article.status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    public Status getStatus() {
        return status;
    }

    public void update(String userId, String content) throws UnauthorizedRequestException {
        if (!writer.equals(userId))
            throw new UnauthorizedRequestException();
        this.content = content;
    }

    public void delete(String userId) throws UnauthorizedRequestException {
        if (!writer.equals(userId))
            throw new UnauthorizedRequestException();
        this.status = Status.DELETED;
    }
}
