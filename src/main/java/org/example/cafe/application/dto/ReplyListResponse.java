package org.example.cafe.application.dto;

import static org.example.cafe.application.ReplyService.REPLY_PAGE_SIZE;
import static org.example.cafe.utils.DateTimeFormatUtils.formatDateTime;

import java.util.List;
import org.example.cafe.domain.Reply;

public record ReplyListResponse(List<ReplyResponse> data,
                                Cursor cursor) {

    public static ReplyListResponse create(List<Reply> replies) {
        List<ReplyResponse> replyResponseList = replies.stream().map(ReplyResponse::from).toList();
        if (replyResponseList.isEmpty()) {
            return new ReplyListResponse(replyResponseList, new Cursor(null, null, false));
        }

        ReplyResponse lastReplyResponse = replyResponseList.get(replyResponseList.size() - 1);
        Cursor cursor = new Cursor(lastReplyResponse.replyId(), lastReplyResponse.createdAt(),
                replyResponseList.size() == REPLY_PAGE_SIZE);
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