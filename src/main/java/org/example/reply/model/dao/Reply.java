package org.example.reply.model.dao;

import java.time.LocalDateTime;
import org.example.reply.model.ReplyStatus;

public class Reply {

    private Long id;
    private Long postId;
    private String writer;
    private String contents;
    private ReplyStatus replyStatus;
    private LocalDateTime createdAt;

    public static Reply create(String writer, String contents) {
        Reply reply = new Reply();
        reply.writer = writer;
        reply.contents = contents;
        reply.replyStatus = ReplyStatus.AVAILABLE;
        reply.createdAt = LocalDateTime.now();
        reply.validate();
        return reply;
    }

    public static Reply createWithId(Long id, Long postId, String writer, String title, String contents) {
        Reply reply = new Reply();
        reply.id = id;
        reply.postId = postId;
        reply.writer = writer;
        reply.contents = contents;
        reply.createdAt = LocalDateTime.now();
        reply.validate();
        return reply;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public ReplyStatus getReplyStatus() {
        return replyStatus;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", postId=" + postId +
                ", writer='" + writer + '\'' +
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
        if (writer == null || writer.trim().isEmpty()) {
            throw new IllegalArgumentException("writer cannot be null or empty");
        }
    }
}
