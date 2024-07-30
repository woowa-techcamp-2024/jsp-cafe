package cafe.domain.entity;

public class Comment {
    private final String commentId;
    private final String userId;
    private final String postId;
    private final String contents;

    public static Comment of(String commentId, String userId, String postId, String contents) {
        return new Comment(commentId, userId, postId, contents);
    }

    private Comment(String commentId, String userId, String postId, String contents) {
        validate(commentId, userId, postId, contents);
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.contents = contents;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public String getContents() {
        return contents;
    }

    private void validate(String commentId, String userId, String postId, String contents) {
        if (commentId == null || commentId.isEmpty()) {
            throw new IllegalArgumentException("CommentId is empty!");
        }
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("UserId is empty!");
        }
        if (postId == null || postId.isEmpty()) {
            throw new IllegalArgumentException("PostId is empty!");
        }
        if (contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("Contents is empty!");
        }
    }
}
