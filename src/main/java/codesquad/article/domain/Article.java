package codesquad.article.domain;

import codesquad.article.domain.vo.Status;
import codesquad.common.exception.UnauthorizedRequestException;

public class Article {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private Status status;

    /**
     * 검증 로직
     *
     * @param title:   5<size<15
     * @param writer:
     * @param content:
     * @param status:
     */
    public Article(String title, String writer, String content, Status status) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.status = status;
    }

    /**
     * 검증 로직
     *
     * @param id:
     * @param title:
     * @param writer:
     * @param content:
     * @param status:
     */
    public Article(Long id, String title, String writer, String content, Status status) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.status = status;
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
