package org.example.reply.model.dto;

import java.time.LocalDateTime;
import org.example.reply.model.ReplyStatus;
import org.example.reply.model.dao.Reply;

public class ReplyDto {

    private Long id;
    private Long postId;
    private String writer;
    private String contents;
    private ReplyStatus replyStatus;
    private LocalDateTime createdAt;

    public static ReplyDto toDto(Reply reply) {
        ReplyDto dto = new ReplyDto();
        dto.id = reply.getId();
        dto.postId = reply.getPostId();
        dto.writer = reply.getWriter();
        dto.contents = reply.getContents();
        dto.replyStatus = reply.getReplyStatus();
        dto.createdAt = reply.getCreatedAt();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public ReplyStatus getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(ReplyStatus replyStatus) {
        this.replyStatus = replyStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ReplyDto{" +
                "id=" + id +
                ", postId=" + postId +
                ", writer='" + writer + '\'' +
                ", contents='" + contents + '\'' +
                ", replyStatus=" + replyStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
