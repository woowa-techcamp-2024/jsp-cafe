package com.wootecam.jspcafe.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Question {

    private Long id;
    private final String writer;
    private final String title;
    private final String contents;
    private final LocalDateTime createdTime;

    public Question(final String writer, final String title, final String contents, final LocalDateTime createdTime) {
        validate(writer, title, contents, createdTime);
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdTime = createdTime;
    }

    public Question(final Long id, final String writer, final String title, final String contents,
                    final LocalDateTime createdTime) {
        this(writer, title, contents, createdTime);
        this.id = id;
    }

    private void validate(final String writer, final String title, final String contents,
                          final LocalDateTime createdTime) {
        if (Objects.isNull(writer) || Objects.isNull(title) || Objects.isNull(contents) || Objects.isNull(createdTime)
                || writer.isEmpty() || title.isEmpty() || contents.isEmpty()) {
            throw new IllegalArgumentException("질문 작성 시 모든 정보를 입력해야 합니다.");
        }
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}
