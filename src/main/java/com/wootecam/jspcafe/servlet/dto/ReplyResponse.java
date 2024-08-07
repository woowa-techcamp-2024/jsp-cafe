package com.wootecam.jspcafe.servlet.dto;

import com.wootecam.jspcafe.domain.Reply;
import java.time.LocalDateTime;

public class ReplyResponse {

    private Long id;
    private final String writer;
    private final String contents;
    private final LocalDateTime createdTime;

    public ReplyResponse(final Long id, final String writer, final String contents, final LocalDateTime createdTime) {
        this.id = id;
        this.writer = writer;
        this.contents = contents;
        this.createdTime = createdTime;
    }

    public static ReplyResponse from(Reply reply) {
        return new ReplyResponse(reply.getId(), reply.getWriter(), reply.getContents(), reply.getCreatedTime());
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
}
