package org.example.reply.model.dao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.example.reply.model.ReplyStatus;

public class Reply {

    private Long id;
    private Long postId;
    private String userId;
    private String contents;
    private ReplyStatus replyStatus;
    private LocalDateTime createdAt;

    public static Reply create(String userId, Long postId, String contents) {
        Reply reply = new Reply();
        reply.userId = userId;
        reply.postId = postId;
        reply.contents = contents;
        reply.replyStatus = ReplyStatus.AVAILABLE;
        reply.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        reply.validate();
        return reply;
    }

    public static Reply createWithAll(Long id, Long postId, String userId, String contents, ReplyStatus replyStatus,
                                      LocalDateTime createdAt) {
        Reply reply = new Reply();
        reply.id = id;
        reply.postId = postId;
        reply.userId = userId;
        reply.contents = contents;
        reply.replyStatus = replyStatus;
        reply.createdAt = createdAt;
        reply.validate();
        return reply;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getContents() {
        return contents;
    }

    public ReplyStatus getReplyStatus() {
        return replyStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId='" + userId + '\'' +
                ", contents='" + contents + '\'' +
                ", replyStatus=" + replyStatus +
                '}';
    }

    private void validate() {
        if (postId == null) {
            throw new IllegalArgumentException("postId cannot be null");
        }
        if (contents == null || contents.trim().isEmpty()) {
            throw new IllegalArgumentException("contents cannot be null or empty");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("writer cannot be null or empty");
        }
        if (replyStatus == null) {
            throw new IllegalArgumentException("writer cannot be null or empty");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("writer cannot be null or empty");
        }
    }
}
