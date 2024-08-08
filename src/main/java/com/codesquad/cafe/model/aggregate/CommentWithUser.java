package com.codesquad.cafe.model.aggregate;

import com.codesquad.cafe.util.DateTimeFormatUtil;
import java.time.LocalDateTime;

public class CommentWithUser {

    private Long id;

    private Long postId;

    private Long parentId;

    private Long userId;

    private String username;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deleted;

    public CommentWithUser() {
    }

    public CommentWithUser(Long id,
                           Long postId,
                           Long parentId,
                           Long userId,
                           String username,
                           String content,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt,
                           Boolean deleted) {
        this.id = id;
        this.postId = postId;
        this.parentId = parentId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    public String getFormattedDate() {
        return DateTimeFormatUtil.getFormattedDate(createdAt);
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return DateTimeFormatUtil.getFormattedDate(createdAt);
    }

    public String getUpdatedAt() {
        return DateTimeFormatUtil.getFormattedDate(updatedAt);
    }

    public Boolean getDeleted() {
        return deleted;
    }

}
