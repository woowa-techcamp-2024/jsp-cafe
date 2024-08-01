package woowa.camp.jspcafe.domain;

import java.time.LocalDateTime;

public class Reply {

    private Long replyId;
    private Long userId;
    private Long articleId;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Reply(Long userId, Long articleId, String content,
                 LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt
    ) {
        this.userId = userId;
        this.articleId = articleId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    private Reply(Long replyId, Long userId, Long articleId, String content, LocalDateTime createdAt,
                  LocalDateTime updatedAt, LocalDateTime deletedAt
    ) {
        this.replyId = replyId;
        this.userId = userId;
        this.articleId = articleId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Reply update(Reply originReply, String content, LocalDateTime updatedAt) {
        return new Reply(originReply.getReplyId(),
                originReply.getUserId(),
                originReply.getArticleId(),
                content,
                originReply.getCreatedAt(),
                updatedAt,
                originReply.getDeletedAt());
    }

    public Long getReplyId() {
        return replyId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * only allowed at repository, test
     */
    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + replyId +
                ", userId=" + userId +
                ", articleId=" + articleId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
