package org.example.entity;

public class Reply {
    private int replyId;
    private final String content;
    private final String authorId;
    private final int articleId;
    private final boolean isDeleted;


    public Reply(int replyId, String content, String authorId, int articleId, boolean isDeleted) {
        this.replyId = replyId;
        this.content = content;
        this.authorId = authorId;
        this.articleId = articleId;
        this.isDeleted = isDeleted;
    }

    public Reply(String content, String authorId, int articleId) {
        this.content = content;
        this.authorId = authorId;
        this.articleId = articleId;
        this.isDeleted = false;
    }

    public int getReplyId() {
        return replyId;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getArticleId() {
        return articleId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public boolean isOwner(String authorId) {
        return this.authorId.equals(authorId);
    }
}
