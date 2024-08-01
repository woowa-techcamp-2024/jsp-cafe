package codesquad.domain.comment;

import codesquad.exception.UnauthorizedRequestException;

public class Comment {
    private Long id;
    private Long articleId;
    private String writer;
    private String content;
    private Status status;

    public Comment(Long articleId, String writer, String content) {
        this.articleId = articleId;
        this.writer = writer;
        this.content = content;
        this.status = Status.COMMENTED;
    }

    public Comment(Long id, Long articleId, String writer, String content, Status status) {
        this.id = id;
        this.articleId = articleId;
        this.writer = writer;
        this.content = content;
        this.status = status;
    }

    public Comment(Long id, Comment comment) {
        this.id = id;
        this.articleId = comment.articleId;
        this.writer = comment.writer;
        this.content = comment.content;
        this.status = comment.status;
    }

    public Long getId() {
        return id;
    }

    public void delete(String userId) throws UnauthorizedRequestException {
        if (!writer.equals(userId))
            throw new UnauthorizedRequestException();
        this.status = Status.DELETED;
    }
}
