package org.example.domain;

import java.time.LocalDateTime;
import org.example.constance.AliveStatus;

public class Reply {
    private Long replyId;
    private Long userId;
    private Long articleId;
    private String author;
    private String comment;
    private AliveStatus aliveStatus;
    private LocalDateTime createdDt;

    private Reply() {
    }

    public Reply(Long replyId, Long userId, Long articleId, String author, String comment, AliveStatus aliveStatus,
                 LocalDateTime createdDt) {
        this.replyId = replyId;
        this.userId = userId;
        this.articleId = articleId;
        this.author = author;
        this.comment = comment;
        this.aliveStatus = aliveStatus;
        this.createdDt = createdDt;
    }

    public Reply(Long userId, Long articleId, String author, String comment, AliveStatus aliveStatus,
                 LocalDateTime createdDt) {
        this.userId = userId;
        this.articleId = articleId;
        this.author = author;
        this.comment = comment;
        this.aliveStatus = aliveStatus;
        this.createdDt = createdDt;
    }

    public Long getReplyId() {
        return replyId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public AliveStatus getAliveStatus() {
        return aliveStatus;
    }

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }

    public void delete() {
        this.aliveStatus = AliveStatus.DELETED;
    }
}
