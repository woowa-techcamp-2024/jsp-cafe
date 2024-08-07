package com.wootecam.jspcafe.servlet.dto;

import com.wootecam.jspcafe.domain.Reply;
import java.util.List;

public class RepliesPageResponse {

    private final int replyCount;
    private final Long lastReplyId;
    private final List<ReplyResponse> replies;

    public RepliesPageResponse(final int replyCount, final Long lastReplyId, final List<ReplyResponse> replies) {
        this.replyCount = replyCount;
        this.lastReplyId = lastReplyId;
        this.replies = replies;
    }

    public static RepliesPageResponse of(final int replyCount, final List<Reply> replies) {
        List<ReplyResponse> responses = replies.stream()
                .map(ReplyResponse::from)
                .toList();

        Long lastReplyId = -1L;
        if (!replies.isEmpty()) {
            lastReplyId = replies.get(replies.size() - 1).getId();
        }

        return new RepliesPageResponse(replyCount, lastReplyId, responses);
    }

    public int getReplyCount() {
        return replyCount;
    }

    public Long getLastReplyId() {
        return lastReplyId;
    }

    public List<ReplyResponse> getReplies() {
        return replies;
    }
}
