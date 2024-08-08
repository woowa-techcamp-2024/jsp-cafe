package org.example.cafe.application.dto;

import static org.example.cafe.utils.DateTimeFormatUtils.formatDateTime;

import org.example.cafe.domain.Reply;

public record ReplyCreateResponse(Long replyId,
                                  String writer,
                                  String content,
                                  Long questionId,
                                  String createdAt) {

    public ReplyCreateResponse(Reply reply) {
        this(
                reply.getReplyId(),
                reply.getWriter(),
                reply.getContent(),
                reply.getQuestionId(),
                formatDateTime(reply.getCreatedAt())
        );
    }
}
