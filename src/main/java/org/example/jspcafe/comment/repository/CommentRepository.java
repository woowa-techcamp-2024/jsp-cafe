package org.example.jspcafe.comment.repository;

import org.example.jspcafe.Repository;
import org.example.jspcafe.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends Repository<Comment> {
    List<CommentVO> findAllByPostIdsJoinFetch(List<Long> postIds);
    void deleteAllInBatch();
    List<CommentVO> findCommentsJoinUser(Long postId);

    List<Comment> findAllByPostId(Long postId);
}
