package org.example.cafe.application.dto;

import static org.example.cafe.utils.DateTimeFormatUtils.formatDateTime;

import java.util.List;
import org.example.cafe.common.page.Page;
import org.example.cafe.domain.Reply;

public record ReplyListResponse(List<ReplyResponse> data,
                                Cursor cursor) {

    public static ReplyListResponse create(Page<Reply> replies) {
        List<ReplyResponse> replyResponseList = replies.getContent().stream().map(ReplyResponse::from).toList();

        Reply lastItem = replies.getLastItem();
        if (lastItem == null) {
            return new ReplyListResponse(replyResponseList, null);
        }

        Cursor cursor = new Cursor(lastItem.getReplyId(), formatDateTime(lastItem.getCreatedAt()), replies.hasNext());
        return new ReplyListResponse(replyResponseList, cursor);
    }

    record ReplyResponse(Long replyId,
                         String writer,
                         String content,
                         String createdAt) {

        public static ReplyResponse from(Reply reply) {
            return new ReplyResponse(reply.getReplyId(),
                    reply.getWriter(),
                    reply.getContent(),
                    formatDateTime(reply.getCreatedAt()));
        }
    }

    record Cursor(Long replyId,
                  String createdAt,
                  Boolean hasNext) {
    }
}