package codesquad.domain.comment;

import java.util.List;

public interface CommentDao {
    List<Comment> findAllByArticleId(Long articleId);

    void updateStatus(List<Comment> comments, Status status);
}
