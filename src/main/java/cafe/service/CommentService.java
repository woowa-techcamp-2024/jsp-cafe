package cafe.service;

import cafe.domain.db.CommentDatabase;
import cafe.domain.entity.Comment;
import cafe.domain.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CommentService {
    private final CommentDatabase commentDatabase;

    public CommentService(CommentDatabase commentDatabase) {
        this.commentDatabase = commentDatabase;
    }

    public List<Comment> findCommentsByArticleId(String requestURI) {
        String articleId = requestURI.split("/")[2];
        List<Comment> comments = commentDatabase.selectCommentsByArticleId(articleId);
        return Objects.requireNonNull(comments);
    }

    public void save(String requestURI, String comments, String userId) {
        String articleId = requestURI.split("/")[2];
        Comment comment = Comment.of(UUID.randomUUID().toString(), userId, articleId, comments);
        commentDatabase.insert(comment);
    }

    public void deleteById(String requestURI) {
        String commentId = requestURI.split("/")[2];
        commentDatabase.deleteById(commentId);
    }

    public void verifyCommentId(User user, String requestURI) {
        String commentId = requestURI.split("/")[2];
        Comment comment = commentDatabase.selectById(commentId);
        if (!comment.getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("You are not the owner of this comment!");
        }
    }

    public void verifyCommentIdByArticleId(User user, String requestURI) {
        String articleId = requestURI.split("/")[2];
        List<Comment> comments = commentDatabase.selectCommentsByArticleId(articleId);
        if (!comments.stream().allMatch(comment -> comment.getUserId().equals(user.getUserId()))) {
            throw new IllegalArgumentException("You are not the owner of this comment!");
        }
    }

    public void deleteByArticleId(String requestURI) {
        String articleId = requestURI.split("/")[2];
        commentDatabase.deleteByArticleId(articleId);
    }
}
