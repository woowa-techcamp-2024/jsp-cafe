package com.wootecam.jspcafe.servlet.dto;

import com.wootecam.jspcafe.domain.Reply;
import java.util.List;

public class RepliesAppendPageResponse {

    private final Long lastReplyId;
    private final List<ReplyResponse> replies;

    public RepliesAppendPageResponse(final Long lastReplyId, final List<ReplyResponse> replies) {
        this.lastReplyId = lastReplyId;
        this.replies = replies;
    }

    public static RepliesAppendPageResponse of(final List<Reply> replies) {
        List<ReplyResponse> responses = replies.stream()
                .map(ReplyResponse::from)
                .toList();

        Long lastReplyId = -1L;
        if (!replies.isEmpty()) {
            lastReplyId = replies.get(replies.size() - 1).getId();
        }

        return new RepliesAppendPageResponse(lastReplyId, responses);
    }

    public Long getLastReplyId() {
        return lastReplyId;
    }

    public List<ReplyResponse> getReplies() {
        return replies;
    }
}
