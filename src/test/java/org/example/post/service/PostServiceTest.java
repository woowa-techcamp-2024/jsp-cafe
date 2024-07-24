package org.example.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostResponse;
import org.example.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class PostServiceTest {

    PostService postService;

    @BeforeEach
    public void setUp() {
        postService = new PostService(new MockPostRepository());
    }

    @Test
    @DisplayName("유효한 post가 주어졌을 때 저장 후 response를 반환한다.")
    public void create_post_successfully_returns_post_response() throws SQLException {
        // Given
        Post post = Post.create("writer", "title", "contents");

        // When
        PostResponse response = postService.create(post);

        // Then
        assertNotNull(response);
        assertEquals(post.getWriter(), response.getWriter());
        assertEquals(post.getTitle(), response.getTitle());
        assertEquals(post.getContents(), response.getContents());
    }

    @Test
    @DisplayName("포스트가 존재하지 않을ㄷ 때 모든 포스트를 불러오면 빈 리스트가 반환된다.")
    public void get_all_posts_returns_list_of_post_response() throws SQLException {

        // When
        List<PostResponse> responses = postService.getAll();

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
    }

    @Test
    @DisplayName("유효한 id가 주어졌을 때 해당 id로 post를 찾아 response로 변환해 반환한다.")
    public void get_post_by_id_returns_correct_post_response() throws SQLException {
        // Given
        long id = 1L;

        // When
        PostResponse response = postService.getPostById(id);

        // Then
        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    @DisplayName("writer, title, contents 중 null이 존재하면 IllegalArgumentException 을 발생시킨다.")
    public void create_post_with_null_or_empty_fields_throws_exception() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.create(Post.create(null, "title", "contents"));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            postService.create(Post.create("writer", null, "contents"));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            postService.create(Post.create("writer", "title", null));
        });
    }

    @Test
    @DisplayName("존재하지 않는 ID로 get 할 시 예외가 발생한다.")
    public void get_post_by_non_existent_id_throws_sql_exception() {
        // Given
        long nonExistentId = 999L;
        // When & Then
        assertThrows(SQLException.class, () -> {
            postService.getPostById(nonExistentId);
        });
    }


    @Test
    @DisplayName("getAll로 가져온 List는 수정 불가이다.")
    public void validate_immutability_of_list_returned_by_get_all() throws SQLException {
        // When
        List<PostResponse> responses = postService.getAll();

        // Then
        assertThrows(UnsupportedOperationException.class, () -> {
            responses.add(new PostResponse());
        });
    }

    @Test
    @DisplayName("유효한 포스트가 주어졌을 때 response로 매핑이 정상적으로 된다.")
    public void verify_mapping_from_post_to_post_response_is_accurate() {
        // Given
        Post post = Post.create("writer", "title", "contents");

        // When
        PostResponse response = PostResponse.toResponse(post);

        // Then
        assertEquals(post.getId(), response.getId());
        assertEquals(post.getWriter(), response.getWriter());
        assertEquals(post.getTitle(), response.getTitle());
        assertEquals(post.getContents(), response.getContents());
    }

    static class MockPostRepository extends PostRepository {
        @Override
        public Post save(Post post) throws SQLException {
            return post;
        }

        @Override
        public List<Post> findAll() throws SQLException {
            return super.findAll();
        }

        @Override
        public Post findById(Long id) throws SQLException {
            if (id >= 999L) throw new SQLException();
            return Post.createWithId(id, "writer", "title", "contents");
        }
    }

}