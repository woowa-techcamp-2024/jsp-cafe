package org.example.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.example.post.model.PostStatus;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostDto;
import org.example.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @Test
    @DisplayName("게시글 생성 시 저장소에 성공적으로 저장된다")
    public void create_post_saves_in_repository() throws SQLException {
        // Given
        Post post = Post.create("user1", "Title", "Contents");

        // When
        postService.create(post);

        // Then
        verify(postRepository, times(1)).save(post);
    }

    @Test
    @DisplayName("모든 게시글 조회 시 PostDto 목록을 반환한다")
    public void get_all_returns_list_of_PostDto() throws SQLException {
        // Given
        List<PostDto> expectedPosts = Arrays.asList(new PostDto.Builder().build());
        when(postRepository.findAll()).thenReturn(expectedPosts);

        // When
        List<PostDto> actualPosts = postService.getAll();

        // Then
        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    @DisplayName("ID로 게시글 조회 시 올바른 PostDto를 반환한다")
    public void get_post_by_id_returns_correct_PostDto() throws SQLException {
        // Given
        PostDto expectedPost = new PostDto.Builder().id(1L).status(PostStatus.AVAILABLE).build();
        when(postRepository.findById(1L)).thenReturn(expectedPost);

        // When
        PostDto actualPost = postService.getPostById(1L);

        // Then
        assertEquals(expectedPost, actualPost);
    }

    @Test
    @DisplayName("게시글 수정 시 저장소에서 성공적으로 업데이트된다")
    public void update_post_modifies_in_repository() throws SQLException {
        // Given
        Long postId = 1L;
        String userId = "user1";
        String newTitle = "New Title";
        String newContents = "New Contents";
        LocalDateTime createdAt = LocalDateTime.now();

        PostDto postDto = new PostDto.Builder()
                .id(postId)
                .title(newTitle)
                .contents(newContents)
                .status(PostStatus.AVAILABLE)
                .createdAt(createdAt)
                .build();

        Post expectedPost = Post.createWithAll(postId, userId, newTitle, newContents, PostStatus.AVAILABLE, createdAt);

        // When
        postService.updatePost(userId, postDto);

        // Then
        verify(postRepository).update(argThat(actualPost ->
                actualPost.getId().equals(expectedPost.getId()) &&
                        actualPost.getUserId().equals(expectedPost.getUserId()) &&
                        actualPost.getTitle().equals(expectedPost.getTitle()) &&
                        actualPost.getContents().equals(expectedPost.getContents()) &&
                        actualPost.getPostStatus().equals(expectedPost.getPostStatus()) &&
                        actualPost.getCreatedAt().equals(expectedPost.getCreatedAt())
        ));
    }

    @Test
    @DisplayName("ID로 게시글 삭제 시 삭제 상태로 표시된다")
    public void delete_post_by_id_marks_as_deleted() throws SQLException {
        // Given
        // When
        postService.deleteById(1L);

        // Then
        verify(postRepository, times(1)).delete(1L);
    }

    @Test
    @DisplayName("null 필드로 게시글 생성 시 IllegalArgumentException이 발생한다")
    public void create_post_with_null_fields_throws_exception() {
        // Given
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Post.create(null, null, null);
        });
    }

    @Test
    @DisplayName("존재하지 않는 ID로 게시글 조회 시 SQLException이 발생한다")
    public void get_post_by_non_existent_id_throws_exception() throws SQLException {
        // Given
        when(postRepository.findById(999L)).thenThrow(new SQLException("￪ﾲﾌ￬ﾋﾜ￫ﾬﾼ￬ﾝﾄ ￬ﾰﾾ￬ﾝﾄ ￬ﾈﾘ ￬ﾗﾆ￬ﾊﾵ￫ﾋﾈ￫ﾋﾤ."));

        // When & Then
        assertThrows(SQLException.class, () -> {
            postService.getPostById(999L);
        });
    }

    @Test
    @DisplayName("null 필드로 게시글 수정 시 IllegalArgumentException이 발생한다")
    public void update_post_with_null_fields_throws_exception() {
        // Given
        PostService postService = new PostService(postRepository);
        PostDto postDto = new PostDto.Builder().id(1L).title(null).contents(null).status(PostStatus.AVAILABLE).build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost("user1", postDto);
        });
    }

    @Test
    @DisplayName("존재하지 않는 ID로 게시글 삭제 시 SQLException이 발생한다")
    public void delete_post_by_non_existent_id_throws_exception() throws SQLException {
        // Given
        doThrow(new SQLException("Updating post failed, no rows affected.")).when(postRepository).delete(999L);

        // When & Then
        assertThrows(SQLException.class, () -> {
            postService.deleteById(999L);
        });
    }

    @Test
    @DisplayName("삭제된 게시글을 ID로 조회 시 IllegalArgumentException이 발생한다")
    public void get_deleted_post_by_id_throws_exception() throws SQLException {
        // Given
        PostDto deletedPost = new PostDto.Builder().id(1L).status(PostStatus.DELETED).build();
        when(postRepository.findById(1L)).thenReturn(deletedPost);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.getPostById(1L);
        });
    }
}