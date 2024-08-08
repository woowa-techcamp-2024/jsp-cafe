package com.codesquad.cafe.model.aggregate;

import com.codesquad.cafe.util.DateTimeFormatUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDetail {

    private Long postId;

    private String title;

    private String content;

    private String fileName;

    private int view;

    private Long authorId;

    private String authorUsername;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deleted;

    private List<CommentWithUser> comments;

    public PostDetail() {
        comments = new ArrayList<>();
    }

    private PostDetail(Long postId,
                       String title,
                       String content,
                       String fileName,
                       int view,
                       Long authorId,
                       String authorUsername,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt,
                       boolean deleted,
                       List<CommentWithUser> comments) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.view = view;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
        this.comments = comments;
    }

    public PostDetail(PostWithAuthor post, List<CommentWithUser> comments) {
        this(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getFileName(),
                post.getView(),
                post.getAuthorId(),
                post.getAuthorUsername(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.isDeleted(),
                comments
        );

    }

    public String getFormattedDate() {
        return DateTimeFormatUtil.getFormattedDate(createdAt);
    }

    public Long getPostId() {
        return postId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<CommentWithUser> getComments() {
        return comments;
    }

    public List<Long> getCommentIds() {
        List<Long> commentIds = new ArrayList<>();
        for (CommentWithUser comment : comments) {
            commentIds.add(comment.getId());
        }
        return commentIds;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setView(int view) {
        this.view = view;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void addComment(CommentWithUser comment) {
        this.comments.add(comment);
    }
}
