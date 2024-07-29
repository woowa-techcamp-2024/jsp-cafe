package org.example.jspcafe.comment.service;

import org.example.jspcafe.comment.model.Comment;
import org.example.jspcafe.comment.repository.CommentRepository;
import org.example.jspcafe.comment.request.CommentCreateRequest;

import java.time.LocalDateTime;

public class CommentService {

    private final CommentRepository commentRepository;
    
    public void createComment(CommentCreateRequest request) {
        // 댓글 생성 로직
        final Long postId = request.postId();
        final Long userId = request.userId();
        final String content = request.content();

        final Comment comment = new Comment(postId, userId, content, LocalDateTime.now());

        commentRepository.save(comment);
    }

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
