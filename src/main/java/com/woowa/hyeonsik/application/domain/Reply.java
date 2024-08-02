package com.woowa.hyeonsik.application.domain;

import java.time.LocalDateTime;

public final class Reply {
    private static final int WRITER_MAX_LENGTH = 30;
    private static final int CONTENT_MAX_LENGTH = 200;
    private Long id;
    private final Long articleId;
    private final String writer;
    private final String contents;
    private final LocalDateTime createdAt;

    public Reply(Long id, Long articleId, String writer, String contents, LocalDateTime createdAt) {
        this.id = id;
        this.articleId = articleId;
        this.writer = writer;
        this.contents = contents;
        this.createdAt = createdAt;
        validate();
    }

    public Reply(Long id, Long articleId, String writer, String contents) {
        this.id = id;
        this.articleId = articleId;
        this.writer = writer;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();
        validate();
    }

    private void validate() {
        if (articleId == null) {
            throw new IllegalArgumentException("게시글 번호는 필수 입력 항목입니다.");
        }
        if (articleId < 0) {
            throw new IllegalArgumentException("게시글 번호는 음수 일 수 없습니다. 현재: " + articleId);
        }
        if (writer == null || writer.trim().isEmpty()) {
            throw new IllegalArgumentException("작성자는 필수 입력 항목입니다.");
        }
        if (writer.length() > WRITER_MAX_LENGTH) {
            throw new IllegalArgumentException("작성자 이름은 " + WRITER_MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
        if (contents == null || contents.trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 필수 입력 항목입니다.");
        }
        if (contents.length() > CONTENT_MAX_LENGTH) {
            throw new IllegalArgumentException("내용은 " + CONTENT_MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
