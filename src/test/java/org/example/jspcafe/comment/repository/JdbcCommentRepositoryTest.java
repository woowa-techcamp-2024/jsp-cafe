package org.example.jspcafe.comment.repository;

import org.example.jspcafe.AbstractRepositoryTestSupport;
import org.example.jspcafe.comment.model.Comment;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;


class JdbcCommentRepositoryTest extends AbstractRepositoryTestSupport {

    private CommentRepository commentRepository = new JdbcCommentRepository(super.connectionManager);
    private UserRepository userRepository = new JdbcUserRepository(super.connectionManager);


    @Override
    protected void deleteAllInBatch() {
        commentRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("user 닉네임을 조인하여 댓글을 조회할 수 있다")
    @Test
    void findCommentsJoinUser() {
        // given
        System.out.println("commentRepository.findCommentsJoinUser(1L) = " + commentRepository.findCommentsJoinUser(1L));
        List<User> users = List.of(
                new User("user1", "email1@example.com", "password1", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new User("user2", "email2@example.com", "password2", LocalDateTime.of(2021, 1, 2, 0, 0)),
                new User("user3", "email3@example.com", "password3", LocalDateTime.of(2021, 1, 3, 0, 0))
        );

        users.forEach(userRepository::save);

        Long postId = 1L;

        List<Comment> comments = List.of(
                new Comment(postId, 1L, "content1", LocalDateTime.of(2021, 1, 1, 0, 0)),
                new Comment(postId, 2L, "content2", LocalDateTime.of(2021, 1, 2, 0, 0)),
                new Comment(postId, 3L, "content3", LocalDateTime.of(2021, 1, 3, 0, 0))
        );

        comments.forEach(commentRepository::save);


        // when
        List<CommentVO> result = commentRepository.findCommentsJoinUser(postId);

        // then
        assertThat(result)
                .extracting("postId", "userId", "nickname", "content", "createdAt")
                .containsExactlyInAnyOrder(
                        tuple(postId, 1L, "user1", "content1", LocalDateTime.of(2021, 1, 1, 0, 0)),
                        tuple(postId, 2L, "user2", "content2", LocalDateTime.of(2021, 1, 2, 0, 0)),
                        tuple(postId, 3L, "user3", "content3", LocalDateTime.of(2021, 1, 3, 0, 0))
                );
    }

//    @DisplayName("주어진 게시물 ID 목록에 대한 모든 댓글을 조회할 수 있다")
//    @Test
//    void findAllByPostIdsJoinFetch() {
//        // given
//        System.out.println("commentRepository.findCommentsJoinUser(1L) = " + commentRepository.findCommentsJoinUser(1L));
//
//        List<User> users = List.of(
//                new User("user1", "email1@example.com", "password1", LocalDateTime.of(2021, 1, 1, 0, 0)),
//                new User("user2", "email2@example.com", "password2", LocalDateTime.of(2021, 1, 2, 0, 0)),
//                new User("user3", "email3@example.com", "password3", LocalDateTime.of(2021, 1, 3, 0, 0))
//        );
//
//        users.forEach(userRepository::save);
//
//
//        List<Comment> comments = List.of(
//                new Comment(1L, 1L, "content1", LocalDateTime.of(2021, 1, 1, 0, 0)),
//                new Comment(1L, 2L, "content2", LocalDateTime.of(2021, 1, 2, 0, 0)),
//                new Comment(2L, 3L, "content3", LocalDateTime.of(2021, 1, 3, 0, 0))
//        );
//
//        comments.forEach(commentRepository::save);
//
//        // when
//        List<CommentVO> result = commentRepository.findAllByPostIdsJoinFetch(List.of(1L, 2L));
//
//        System.out.println("result = " + result);
//        // then
//        assertThat(result)
//                .extracting("postId", "userId", "nickname", "content", "createdAt")
//                .containsExactlyInAnyOrder(
//                        tuple(1L, 1L, "user1", "content1", LocalDateTime.of(2021, 1, 1, 0, 0)),
//                        tuple(1L, 2L, "user2", "content2", LocalDateTime.of(2021, 1, 2, 0, 0)),
//                        tuple(2L, 3L, "user3", "content3", LocalDateTime.of(2021, 1, 3, 0, 0))
//                );
//    }

//    @DisplayName("빈 게시물 ID 목록을 제공하면 빈 결과를 반환한다")
//    @Test
//    void findAllByPostIdsJoinFetchWithEmptyList() {
//        // when
//        List<CommentVO> result = commentRepository.findAllByPostIdsJoinFetch(List.of());
//
//        // then
//        assertThat(result).isEmpty();
//    }

    @DisplayName("기존 댓글을 저장하면 기존 댓글을 반환한다")
    @Test
    void saveAlreadyExists() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "content";
        Comment comment = new Comment(postId, userId, content, LocalDateTime.of(2021, 1, 1, 0, 0));
        Comment savedComment = commentRepository.save(comment);

        // when
        Comment savedComment2 = commentRepository.save(savedComment);

        // then
        assertThat(savedComment2).isNotNull()
                .extracting("postId", "userId", "content.value")
                .contains(postId, userId, content);
    }

    @DisplayName("댓글을 저장하고 조회할 수 있다")
    @Test
    void saveAndFindById() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "content";
        Comment comment = new Comment(postId, userId, content, LocalDateTime.of(2021, 1, 1, 0, 0));

        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        Long savedCommentId = savedComment.getCommentId();
        Optional<Comment> foundComment = commentRepository.findById(savedCommentId);

        assertThat(foundComment).isPresent()
                .get()
                .extracting("postId", "userId", "content.value")
                .contains(postId, userId, content);
    }

    @DisplayName("저장되지 않은 댓글을 조회할 때 null을 반환한다")
    @Test
    void findByIdNotFound() {
        // when
        Optional<Comment> foundComment = commentRepository.findById(1L);

        // then
        assertThat(foundComment).isNotPresent();
    }

    @DisplayName("댓글을 업데이트할 수 있다")
    @Test
    void update() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "content";
        Comment comment = new Comment(postId, userId, content, LocalDateTime.of(2021, 1, 1, 0, 0));

        Comment savedComment = commentRepository.save(comment);
        savedComment.modifyContent("newContent");

        // when
        commentRepository.update(savedComment);

        // then
        Optional<Comment> updatedComment = commentRepository.findById(savedComment.getCommentId());
        assertThat(updatedComment).isPresent()
                .get()
                .extracting("postId", "userId", "content.value", "createdAt")
                .contains(postId, userId, "newContent", LocalDateTime.of(2021, 1, 1, 0, 0));
    }

    @DisplayName("댓글을 삭제할 수 있다")
    @Test
    void delete() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "content";
        Comment comment = new Comment(postId, userId, content, LocalDateTime.of(2021, 1, 1, 0, 0));
        Comment savedComment = commentRepository.save(comment);

        // when
        commentRepository.delete(savedComment);
        Optional<Comment> deletedComment = commentRepository.findById(savedComment.getCommentId());

        // then
        assertThat(deletedComment).isNotPresent();
    }

    @DisplayName("null 엔티티를 저장할 때 예외를 던진다")
    @Test
    void saveNullEntity() {
        // when & then
        assertThatThrownBy(() -> commentRepository.save(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Entity는 null일 수 없습니다.");
    }

    @DisplayName("ID가 null인 엔티티를 삭제할 때 예외를 던진다")
    @Test
    void deleteEntityWithNullId() {
        // given
        Comment comment = new Comment(1L, 1L, "content", LocalDateTime.of(2021, 1, 1, 0, 0));

        // when & then
        assertThatThrownBy(() -> commentRepository.delete(comment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID는 null일 수 없습니다.");
    }

    @DisplayName("ID가 null인 엔티티를 업데이트할 때 예외를 던진다")
    @Test
    void updateEntityWithNullId() {
        // given
        Comment comment = new Comment(1L, 1L, "content", LocalDateTime.of(2021, 1, 1, 0, 0));

        // when // then
        assertThatThrownBy(() -> commentRepository.update(comment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID는 null일 수 없습니다.");
    }

    @DisplayName("댓글 저장 시 PK가 할당된다")
    @Test
    void saveAssignsPK() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "content";
        Comment comment = new Comment(postId, userId, content, LocalDateTime.of(2021, 1, 1, 0, 0));

        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        assertThat(savedComment)
                .isNotNull()
                .extracting(Comment::getCommentId)
                .isNotNull();
    }

}