package com.woowa.hyeonsik.application.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Article {
    private static final int WRITER_MAX_LENGTH = 30;
    private static final int TITLE_MAX_LENGTH = 30;
    private static final int CONTENT_MAX_LENGTH = 200;
    private Long id;
    private final String writer;
    private final String title;
    private final String contents;
    private final LocalDateTime createdAt;

    public Article(Long id, String writer, String title, String contents) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();
        validate();
    }

    public Article(Long id, String writer, String title, String contents, LocalDateTime localDateTime) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdAt = localDateTime;
        validate();
    }

    private void validate() {
        if (writer == null || writer.trim().isEmpty()) {
            throw new IllegalArgumentException("작성자는 필수 입력 항목입니다.");
        }
        if (writer.length() > WRITER_MAX_LENGTH) {
            throw new IllegalArgumentException("작성자 이름은 " + WRITER_MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 항목입니다.");
        }
        if (title.length() > TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException("제목은 " + TITLE_MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
        if (contents == null || contents.trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 필수 입력 항목입니다.");
        }
        if (contents.length() > CONTENT_MAX_LENGTH) {
            throw new IllegalArgumentException("내용은 " + CONTENT_MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Article) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.writer, that.writer) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, title, contents);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
