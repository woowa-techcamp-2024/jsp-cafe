package com.wootecam.jspcafe.domain;

import com.wootecam.jspcafe.exception.BadRequestException;
import java.time.LocalDateTime;
import java.util.Objects;

public class Question {

    private Long id;
    private final String writer;
    private final String title;
    private final String contents;
    private final LocalDateTime createdTime;
    private final Long userPrimaryId;

    public Question(final String writer, final String title, final String contents, final LocalDateTime createdTime,
                    final Long userPrimaryId) {
        validate(writer, title, contents, createdTime, userPrimaryId);
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdTime = createdTime;
        this.userPrimaryId = userPrimaryId;
    }

    public Question(final Long id, final String writer, final String title, final String contents,
                    final LocalDateTime createdTime, final Long userPrimaryId) {
        this(writer, title, contents, createdTime, userPrimaryId);
        this.id = id;
    }

    private void validate(final String writer, final String title, final String contents,
                          final LocalDateTime createdTime, final Long userPrimaryId) {
        if (Objects.isNull(writer) || Objects.isNull(title) || Objects.isNull(contents) || Objects.isNull(createdTime)
                || Objects.isNull(userPrimaryId) || writer.isEmpty() || title.isEmpty() || contents.isEmpty()) {
            throw new BadRequestException("질문 작성 시 모든 정보를 입력해야 합니다.");
        }
    }

    public boolean isSameWriter(final Long userPrimaryId) {
        return this.userPrimaryId.equals(userPrimaryId);
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

    public Long getUserPrimaryId() {
        return userPrimaryId;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdTime=" + createdTime +
                ", userPrimaryId=" + userPrimaryId +
                '}';
    }
}
