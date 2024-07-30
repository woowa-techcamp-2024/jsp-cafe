package org.example.jspcafe.comment.service;

import org.example.jspcafe.AbstractRepositoryTestSupport;
import org.example.jspcafe.comment.model.Comment;
import org.example.jspcafe.comment.repository.CommentRepository;
import org.example.jspcafe.comment.repository.JdbcCommentRepository;
import org.example.jspcafe.comment.request.CommentCreateRequest;
import org.example.jspcafe.comment.request.CommentDeleteRequest;
import org.example.jspcafe.comment.request.CommentModifyRequest;
import org.example.jspcafe.post.response.CommentResponse;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest extends AbstractRepositoryTestSupport {

    private final CommentRepository commentRepository = new JdbcCommentRepository(super.connectionManager);
    private final UserRepository userRepository = new JdbcUserRepository(super.connectionManager);
    private final CommentService commentService = new CommentService(commentRepository, userRepository);
    @Override
    protected void deleteAllInBatch() {
        commentRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @DisplayName("Comment를 생성할 수 있다.")
    @Test
    void create() {
        // given
        User user = userRepository.save(new User("user1", "email@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0)));

        Long postId = 2L;
        String content = "content";

        CommentCreateRequest request = new CommentCreateRequest(postId, user.getUserId(), content);

        // when
        CommentResponse response = commentService.createComment(request);

        // then
        assertThat(response).isNotNull()
                .extracting("postId", "userId", "content")
                .contains(postId, user.getUserId(), content);
    }

    @DisplayName("없는 사용자로 Comment를 생성할 수 없다.")
    @Test
    void createWithNotExistsUser() {
        // given
        Long postId = 2L;
        Long invalidUserId = 100L;
        String content = "content";

        CommentCreateRequest request = new CommentCreateRequest(postId, invalidUserId, content);

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @DisplayName("Comment를 삭제할 수 있다.")
    @Test
    void delete() {
        // given
        User user = userRepository.save(new User("user1", "email@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0)));

        Comment comment = commentRepository.save(new Comment(1L, user.getUserId(), "content", LocalDateTime.of(2021, 1, 1, 0, 0)));

        CommentDeleteRequest request = new CommentDeleteRequest(comment.getCommentId(), user.getUserId());
        // when
        commentService.deleteComment(request);

        // then
        assertThat(commentRepository.findById(comment.getCommentId())).isEmpty();
    }

    @DisplayName("없는 Comment를 삭제할 수 없다.")
    @Test
    void deleteWithNotExistsComment() {
        // given
        Long invalidCommentId = 100L;

        CommentDeleteRequest request = new CommentDeleteRequest(invalidCommentId, 1L);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글을 찾을 수 없습니다.");
    }

    @DisplayName("다른 사용자의 Comment를 삭제할 수 없다.")
    @Test
    void deleteWithDifferentUser() {
        // given
        User user = userRepository.save(new User("user1", "email@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0)));

        Comment comment = commentRepository.save(new Comment(1L, user.getUserId(), "content", LocalDateTime.of(2021, 1, 1, 0, 0)));

        Long invalidUserId = 100L;

        CommentDeleteRequest request = new CommentDeleteRequest(comment.getCommentId(), invalidUserId);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글을 삭제할 권한이 없습니다.");
    }

    @DisplayName("Comment를 수정할 수 있다.")
    @Test
    void modify() {
        // given
        User user = userRepository.save(new User("user1", "email@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0)));

        Comment comment = commentRepository.save(new Comment(1L, user.getUserId(), "content", LocalDateTime.of(2021, 1, 1, 0, 0)));

        String modifiedContent = "modified content";

        CommentModifyRequest request = new CommentModifyRequest(comment.getCommentId(), user.getUserId(), modifiedContent);

        // when
        commentService.modifyComment(request);

        // then
        Comment modifiedComment = commentRepository.findById(comment.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        assertThat(modifiedComment)
                .extracting("content.value", "userId", "commentId")
                .contains(modifiedContent, user.getUserId(), comment.getCommentId());
    }

    @DisplayName("없는 Comment를 수정할 수 없다.")
    @Test
    void modifyWithNotExistsComment() {
        // given
        Long invalidCommentId = 100L;

        CommentModifyRequest request = new CommentModifyRequest(invalidCommentId, 1L, "content");

        // when & then
        assertThatThrownBy(() -> commentService.modifyComment(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글을 찾을 수 없습니다.");
    }

    @DisplayName("다른 사용자의 Comment를 수정할 수 없다.")
    @Test
    void modifyWithDifferentUser() {
        // given
        User user = userRepository.save(new User("user1", "email@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0)));

        Comment comment = commentRepository.save(new Comment(1L, user.getUserId(), "content", LocalDateTime.of(2021, 1, 1, 0, 0)));

        Long invalidUserId = 100L;

        CommentModifyRequest request = new CommentModifyRequest(comment.getCommentId(), invalidUserId, "content");

        // when & then
        assertThatThrownBy(() -> commentService.modifyComment(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글을 수정할 권한이 없습니다.");
    }

}