package codesquad.comment.repository;

import codesquad.comment.domain.Comment;
import codesquad.comment.domain.vo.Status;

import java.util.List;

public interface CommentRepository {
    List<Comment> findAllByArticleId(Long articleId);

    void updateStatus(List<Comment> comments, Status status);
}
