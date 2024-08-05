package org.example.jspcafe.comment.repository;

import org.example.jspcafe.Repository;
import org.example.jspcafe.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends Repository<Comment> {
    void deleteAllInBatch();
    List<CommentVO> findCommentsJoinUser(Long postId, int limit, int offset);
    int count(Long postId);
    List<Comment> findAllByPostId(Long postId);
}
