package com.wootecam.jspcafe.model;

import java.util.Objects;

public class Question {

    private Long id;
    private final String writer;
    private final String title;
    private final String contents;

    public Question(final String writer, final String title, final String contents) {
        validate(writer, title, contents);
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    private void validate(final String writer, final String title, final String contents) {
        if (Objects.isNull(writer) || Objects.isNull(title) || Objects.isNull(contents)
                || writer.isEmpty() || title.isEmpty() || contents.isEmpty()) {
            throw new IllegalArgumentException("질문 작성 시 모든 정보를 입력해야 합니다.");
        }
    }

    public Question(final Long id, final String writer, final String title, final String contents) {
        this(writer, title, contents);
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

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
