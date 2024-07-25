package org.example.jspcafe.post.repository;

import org.example.jspcafe.AbstractRepositoryTestSupport;
import org.example.jspcafe.post.model.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class JdbcPostRepositoryTest extends AbstractRepositoryTestSupport {

    private PostRepository postRepository = new JdbcPostRepository(super.connectionManager);

    @Override
    protected void deleteAllInBatch() {
        postRepository.deleteAllInBatch();
    }

    @DisplayName("기존 포스트를 저장하면 기존 포스트를 반환한다")
    @Test
    void saveAlreadyExists() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        Post post = new Post(userId, title, content, LocalDateTime.of(2021, 1, 1, 0, 0));
        Post savedPost = postRepository.save(post);

        // when
        Post savedPost2 = postRepository.save(savedPost);

        // then
        assertThat(savedPost2).isNotNull()
                .extracting(p -> p.getUserId(), p -> p.getTitle().getValue(), p -> p.getContent().getValue())
                .contains(userId, title, content);
    }

    @DisplayName("포스트를 저장하고 조회할 수 있다")
    @Test
    void saveAndFindById() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        Post post = new Post(userId, title, content, LocalDateTime.of(2021, 1, 1, 0, 0));

        // when
        Post savedPost = postRepository.save(post);

        // then
        Long savedPostId = savedPost.getPostId();
        Optional<Post> foundPost = postRepository.findById(savedPostId);

        assertThat(foundPost).isPresent()
                .get()
                .extracting("postId", "userId", "title.value", "content.value")
                .contains(savedPostId, userId, title, content);
    }

    @DisplayName("저장되지 않은 포스트를 조회할 때 null을 반환한다")
    @Test
    void findByIdNotFound() {
        // when
        Optional<Post> foundPost = postRepository.findById(1L);

        // then
        assertThat(foundPost).isNotPresent();
    }

    @DisplayName("포스트를 업데이트할 수 있다")
    @Test
    void update() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        Post post = new Post(userId, title, content, LocalDateTime.of(2021, 1, 1, 0, 0));

        Post savedPost = postRepository.save(post);
        savedPost.updateTitle("newTitle");
        savedPost.updateContent("newContent");

        // when
        postRepository.update(savedPost);

        // then
        Optional<Post> updatedPost = postRepository.findById(savedPost.getPostId());
        assertThat(updatedPost).isPresent()
                .get()
                .extracting("title.value", "content.value")
                .contains("newTitle", "newContent");
    }

    @DisplayName("포스트를 삭제할 수 있다")
    @Test
    void delete() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        Post post = new Post(userId, title, content, LocalDateTime.of(2021, 1, 1, 0, 0));
        Post savedPost = postRepository.save(post);

        // when
        postRepository.delete(savedPost);
        Optional<Post> deletedPost = postRepository.findById(savedPost.getPostId());

        // then
        assertThat(deletedPost).isNotPresent();
    }

    @DisplayName("null 엔티티를 저장할 때 예외를 던진다")
    @Test
    void saveNullEntity() {
        // when & then
        assertThatThrownBy(() -> postRepository.save(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Entity는 null일 수 없습니다.");
    }

    @DisplayName("ID가 null인 엔티티를 삭제할 때 예외를 던진다")
    @Test
    void deleteEntityWithNullId() {
        // given
        Post post = new Post(1L, "title", "content", LocalDateTime.of(2021, 1, 1, 0, 0));

        // when & then
        assertThatThrownBy(() -> postRepository.delete(post))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id는 null일 수 없습니다.");
    }

    @DisplayName("ID가 null인 엔티티를 업데이트할 때 예외를 던진다")
    @Test
    void updateEntityWithNullId() {
        // given
        Post post = new Post(1L, "title", "content", LocalDateTime.of(2021, 1, 1, 0, 0));

        // when // then
        assertThatThrownBy(() -> postRepository.update(post))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id는 null일 수 없습니다.");
    }

    @DisplayName("포스트 저장 시 PK가 할당된다")
    @Test
    void saveAssignsPK() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        Post post = new Post(userId, title, content, LocalDateTime.of(2021, 1, 1, 0, 0));

        // when
        Post savedPost = postRepository.save(post);

        // then
        assertThat(savedPost)
                .isNotNull()
                .extracting(Post::getPostId)
                .isNotNull();
    }

    @DisplayName("전체 게시글을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        List<Post> posts = List.of(
                new Post(1L, "title1", "content1", LocalDateTime.of(2021, 1, 1, 0, 0, 0)),
                new Post(2L, "title2", "content2", LocalDateTime.of(2021, 1, 2, 0, 0, 0)),
                new Post(3L, "title3", "content3", LocalDateTime.of(2021, 1, 3, 0, 0, 0))
        );
        posts.forEach(postRepository::save);

        // when
        List<Post> result = postRepository.findAll();

        // then
        assertThat(result)
                .extracting("userId", "title.value", "content.value", "createdAt")
                .containsExactlyInAnyOrder(
                        tuple(1L, "title1", "content1", LocalDateTime.of(2021, 1, 1, 0, 0, 0)),
                        tuple(2L, "title2", "content2", LocalDateTime.of(2021, 1, 2, 0, 0, 0)),
                        tuple(3L, "title3", "content3", LocalDateTime.of(2021, 1, 3, 0, 0, 0))
                );
    }

    @DisplayName("offset과 limit을 이용하여 게시글을 조회하면 최신 순으로 정렬되어 조회된다.")
    @Test
    void findAllWithOffsetAndLimit() {
        // given
        List<Post> posts = List.of(
                new Post(1L, "title1", "content1", LocalDateTime.of(2021, 1, 1, 0, 0, 0)),
                new Post(2L, "title2", "content2", LocalDateTime.of(2021, 1, 2, 0, 0, 0)),
                new Post(3L, "title3", "content3", LocalDateTime.of(2021, 1, 3, 0, 0, 0)),
                new Post(4L, "title4", "content4", LocalDateTime.of(2021, 1, 4, 0, 0, 0)),
                new Post(5L, "title5", "content5", LocalDateTime.of(2021, 1, 5, 0, 0, 0)),
                new Post(6L, "title6", "content6", LocalDateTime.of(2021, 1, 6, 0, 0, 0))
        );
        posts.forEach(postRepository::save);

        int offset = 2;
        int limit = 3;

        // when
        List<Post> result = postRepository.findAll(offset, limit);

        // then
        assertThat(result)
                .hasSize(3)
                .extracting("userId", "title.value", "content.value", "createdAt")
                .containsExactly(
                        tuple(4L, "title4", "content4", LocalDateTime.of(2021, 1, 4, 0, 0, 0)),
                        tuple(3L, "title3", "content3", LocalDateTime.of(2021, 1, 3, 0, 0, 0)),
                        tuple(2L, "title2", "content2", LocalDateTime.of(2021, 1, 2, 0, 0, 0))
                );
    }

    @DisplayName("음수인 offset으로 조회하면 예외가 발생한다.")
    @Test
    void findAllWithNegativeOffset() {
        // given
        int offset = -1;
        int limit = 3;

        // when & then
        assertThatThrownBy(() -> postRepository.findAll(offset, limit))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("offset과 limit은 0 이상이어야 합니다.");

    }

    @DisplayName("음수인 limit 조회하면 예외가 발생한다.")
    @Test
    void findAllWithNegativeLimit() {
        // given
        int offset = 3;
        int limit = -1;

        // when & then
        assertThatThrownBy(() -> postRepository.findAll(offset, limit))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("offset과 limit은 0 이상이어야 합니다.");

    }


}