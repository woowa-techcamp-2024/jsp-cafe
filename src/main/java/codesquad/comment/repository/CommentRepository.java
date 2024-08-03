package codesquad.comment.repository;

import codesquad.comment.domain.Comment;
import codesquad.comment.domain.vo.Status;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Long save(Comment comment);

    Optional<Comment> findById(Long id);

    List<Comment> findAllByArticleId(Long articleId);

    void update(Comment comment);

    void updateStatus(List<Comment> comments, Status status);
}
