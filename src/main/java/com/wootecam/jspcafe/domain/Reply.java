package com.wootecam.jspcafe.domain;

import com.wootecam.jspcafe.exception.BadRequestException;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reply {

    private Long id;
    private final String writer;
    private final String contents;
    private final LocalDateTime createdTime;
    private final Long userPrimaryId;
    private final Long questionPrimaryId;

    public Reply(final Long id, final String writer, final String contents, final LocalDateTime createdTime,
                 final Long userPrimaryId, final Long questionPrimaryId) {
        this(writer, contents, createdTime, userPrimaryId, questionPrimaryId);
        this.id = id;
    }

    public Reply(final String writer, final String contents, final LocalDateTime createdTime, final Long userPrimaryId,
                 final Long questionPrimaryId) {
        validate(writer, contents, createdTime, userPrimaryId, questionPrimaryId);
        this.writer = writer;
        this.contents = contents;
        this.createdTime = createdTime;
        this.userPrimaryId = userPrimaryId;
        this.questionPrimaryId = questionPrimaryId;
    }

    private void validate(final String writer, final String contents, final LocalDateTime createdTime,
                          final Long userPrimaryId, final Long questionPrimaryId) {
        if (Objects.isNull(writer) || Objects.isNull(contents) || Objects.isNull(createdTime) || Objects.isNull(
                userPrimaryId) || Objects.isNull(questionPrimaryId) || writer.isEmpty() || contents.isEmpty()) {
            throw new BadRequestException("댓글 작성시 모든 정보를 입력해야 합니다.");
        }
    }

    public boolean isWriter(final Long userPrimaryId) {
        return this.userPrimaryId.equals(userPrimaryId);
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public Long getUserPrimaryId() {
        return userPrimaryId;
    }

    public Long getQuestionPrimaryId() {
        return questionPrimaryId;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", contents='" + contents + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}
