package codesqaud.app.model;

import codesqaud.app.exception.HttpException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class Article {
    private Long id;
    private String title;
    private String contents;
    private Long authorId;

    public Article(Long id, String title, String contents, Long authorId) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.authorId = authorId;
    }

    public Article(String title, String contents, Long authorId) {
        validateTitle(title);
        validateContent(contents);
        validateAuthorId(authorId);

        this.title = title;
        this.contents = contents;
        this.authorId = authorId;
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new HttpException(SC_BAD_REQUEST, "제목은 null이거나 비어있을 수 없습니다.");
        }
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new HttpException(SC_BAD_REQUEST, "내용은 null이거나 비어있을 수 없습니다.");
        }
    }

    private void validateAuthorId(Long authorId) {
        if (authorId == null) {
            throw new HttpException(SC_INTERNAL_SERVER_ERROR, "작성자 ID는 null일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    public void setContents(String contents) {
        validateContent(contents);
        this.contents = contents;
    }
}
