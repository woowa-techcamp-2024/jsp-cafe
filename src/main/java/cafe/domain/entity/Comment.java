package cafe.domain.entity;

import java.time.LocalDateTime;

public class Comment {
    private final String commentId;
    private final String userId;
    private final String articleId;
    private final String contents;
    private final String created;

    public static Comment of(String commentId, String userId, String articleId, String contents) {
        return of(commentId, userId, articleId, contents, LocalDateTime.now().toString());
    }

    public static Comment of(String commentId, String userId, String articleId, String contents, String created) {
        return new Comment(commentId, userId, articleId, contents, created);
    }

    private Comment(String commentId, String userId, String articleId, String contents, String created) {
        validate(commentId, userId, articleId, contents);
        this.commentId = commentId;
        this.userId = userId;
        this.articleId = articleId;
        this.contents = contents;
        this.created = created;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getContents() {
        return contents;
    }

    public String getCreated() { return created; }

    private void validate(String commentId, String userId, String articleId, String contents) {
        if (commentId == null || commentId.isEmpty()) {
            throw new IllegalArgumentException("CommentId is empty!");
        }
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("UserId is empty!");
        }
        if (articleId == null || articleId.isEmpty()) {
            throw new IllegalArgumentException("ArticleId is empty!");
        }
        if (contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("Contents is empty!");
        }
    }
}
