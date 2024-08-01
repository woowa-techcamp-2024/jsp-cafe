package woowa.camp.jspcafe.service.dto;

import java.time.LocalDateTime;

public class ReplyResponse {

    private Long replyId;
    private String content;

    private Long userId;
    private String userNickname;

    private String createdAt;

    public ReplyResponse(Long replyId, String content, Long userId, String userNickname, LocalDateTime createdAt) {
        this.replyId = replyId;
        this.content = content;
        this.userId = userId;
        this.userNickname = userNickname;
        this.createdAt = createdAt.toString();
    }

    public Long getReplyId() {
        return replyId;
    }

    public String getContent() {
        return content;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "ReplyResponse{" +
                "replyId=" + replyId +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", userNickname='" + userNickname + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
