package org.example.jspcafe.post.repository;

import org.example.jspcafe.post.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class InMemoryPostRepositoryTest {

    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository = new InMemoryPostRepository();
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

}