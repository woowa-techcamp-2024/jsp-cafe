package codesquad.jspcafe.domain.article.domain;

import codesquad.jspcafe.domain.user.domain.User;
import java.time.LocalDateTime;

public class Article {

    private Long id;
    private final String title;
    private final User writer;
    private final String contents;
    private final LocalDateTime createdAt;

    public Article(String title, User writer, String contents, LocalDateTime createdAt) {
        this.title = verifyTitle(title);
        this.writer = verifyWriter(writer);
        this.contents = verifyContents(contents);
        this.createdAt = createdAt;
    }

    public Article(Long id, String title, User writer, String contents, LocalDateTime createdAt) {
        this(title, writer, contents, createdAt);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    private String verifyTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수 입력값입니다.");
        }
        return title;
    }

    private User verifyWriter(User writer) {
        if (writer == null) {
            throw new IllegalArgumentException("글쓴이는 필수 입력값입니다.");
        }
        return writer;
    }

    private String verifyContents(String contents) {
        if (contents == null || contents.isBlank()) {
            throw new IllegalArgumentException("본문은 필수 입력값입니다.");
        }
        return contents;
    }

}
