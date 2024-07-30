package codesquad.jspcafe.domain.reply.domain;

import codesquad.jspcafe.domain.user.domain.User;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reply {

    private Long id;
    private final Long article;
    private final User writer;
    private String contents;
    private final LocalDateTime createdAt;

    public Reply(Long article, User writer, String contents, LocalDateTime createdAt) {
        try {
            this.article = Objects.requireNonNull(article, "게시글이 존재하지 않습니다.");
            this.writer = Objects.requireNonNull(writer, "작성자가 존재하지 않습니다.");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        this.contents = verifyContents(contents);
        this.createdAt = createdAt;
    }

    public Reply(Long id, Long article, User writer, String contents, LocalDateTime createdAt) {
        this(article, writer, contents, createdAt);
        this.id = id;
    }

    public void update(String contents) {
        this.contents = verifyContents(contents);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticle() {
        return article;
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

    private String verifyContents(String contents) {
        if (contents == null || contents.isBlank()) {
            throw new IllegalArgumentException("댓글 내용이 비어있습니다.");
        }
        return contents;
    }
}
