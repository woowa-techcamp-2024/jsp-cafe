package org.example.jspcafe.comment.service;

import org.example.jspcafe.Component;
import org.example.jspcafe.comment.model.Comment;
import org.example.jspcafe.comment.repository.CommentRepository;
import org.example.jspcafe.comment.repository.CommentVO;
import org.example.jspcafe.comment.request.CommentCreateRequest;
import org.example.jspcafe.post.response.CommentResponse;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentResponse createComment(CommentCreateRequest request) {
        // 댓글 생성 로직
        final Long postId = request.postId();
        final Long userId = request.userId();
        final String content = request.content();

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        final Comment comment = new Comment(postId, userId, content, LocalDateTime.now());

        commentRepository.save(comment);

        return new CommentResponse(
                comment.getCommentId(),
                comment.getPostId(),
                comment.getUserId(),
                user.getNickname().getValue(),
                comment.getContent().getValue(),
                comment.getCreatedAt()
        );
    }

    public CommentService(
            CommentRepository commentRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public List<CommentResponse> findCommentsJoinUser(Long postId) {
        final List<CommentVO> comments = commentRepository.findCommentsJoinUser(postId);
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.commentId(),
                        comment.postId(),
                        comment.userId(),
                        comment.nickname(),
                        comment.content(),
                        comment.createdAt()
                )).toList();
    }
}
