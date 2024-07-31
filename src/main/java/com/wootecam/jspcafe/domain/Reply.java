package com.wootecam.jspcafe.domain;

import java.time.LocalDateTime;

public class Reply {

    private Long id;
    private final String writer;
    private final String contents;
    private final LocalDateTime createdTime;
    private final Long userPrimaryId;
    private final Long questionPrimaryId;

    public Reply(final Long id, final String writer, final String contents, final LocalDateTime createdTime,
                 final Long userPrimaryId, final Long questionPrimaryId) {
        this.id = id;
        this.writer = writer;
        this.contents = contents;
        this.createdTime = createdTime;
        this.userPrimaryId = userPrimaryId;
        this.questionPrimaryId = questionPrimaryId;
    }

    public Reply(final String writer, final String contents, final LocalDateTime createdTime, final Long userPrimaryId,
                 final Long questionPrimaryId) {
        this.writer = writer;
        this.contents = contents;
        this.createdTime = createdTime;
        this.userPrimaryId = userPrimaryId;
        this.questionPrimaryId = questionPrimaryId;
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
