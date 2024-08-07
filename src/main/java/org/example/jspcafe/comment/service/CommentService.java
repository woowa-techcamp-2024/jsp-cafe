package org.example.jspcafe.comment.service;

import org.example.jspcafe.Component;
import org.example.jspcafe.comment.model.Comment;
import org.example.jspcafe.comment.repository.CommentRepository;
import org.example.jspcafe.comment.repository.CommentVO;
import org.example.jspcafe.comment.request.CommentCreateRequest;
import org.example.jspcafe.comment.request.CommentDeleteRequest;
import org.example.jspcafe.comment.request.CommentModifyRequest;
import org.example.jspcafe.post.response.CommentList;
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

    public CommentList findCommentsJoinUserByFirstId(Long postId, long firstCommentId, int size) {
        if (size < 1) {
            size = 5; // 사이즈가 1보다 작으면 기본값 5로 설정
        }

        final List<CommentVO> comments = commentRepository.findCommentsJoinUserByFirstId(postId, firstCommentId, size);
        int totalCount = commentRepository.count(postId);

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> new CommentResponse(
                        comment.commentId(),
                        comment.userId(),
                        comment.postId(),
                        comment.nickname(),
                        comment.content(),
                        comment.createdAt()
                )).toList();

        return new CommentList(commentResponses, totalCount);
    }

    public CommentList findCommentsJoinUser(Long postId, int page, int size) {
        if (page < 1) {
            page = 1; // 페이지 번호가 1보다 작으면 1로 설정
        }
        if (size < 1) {
            size = 5; // 사이즈가 1보다 작으면 기본값 5로 설정
        }
        int offset = (page - 1) * size;

        final List<CommentVO> comments = commentRepository.findCommentsJoinUser(postId, size, offset);
        int totalCount = commentRepository.count(postId);

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> new CommentResponse(
                        comment.commentId(),
                        comment.userId(),
                        comment.postId(),
                        comment.nickname(),
                        comment.content(),
                        comment.createdAt()
                )).toList();
        return new CommentList(commentResponses, totalCount);
    }

    public void deleteComment(final CommentDeleteRequest request) {
        final Long userId = request.userId();
        final Long commentId = request.commentId();

        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    public void modifyComment(final CommentModifyRequest request) {
        final Long userId = request.userId();
        final Long commentId = request.commentId();
        final String content = request.content();

        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.modifyContent(content);
        commentRepository.update(comment);
    }


    public CommentService(
            CommentRepository commentRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }
}
