package org.example.demo.repository;

import org.example.demo.domain.Post;
import org.example.demo.domain.User;
import org.example.demo.model.PostCreateDao;
import org.example.demo.model.UserCreateDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PostRepositoryTest extends RepositoryTestSupport {
    UserRepository userRepository;
    PostRepository postRepository;

    @BeforeEach
    void setUp() {
        setDb();
        userRepository = new UserRepository(dbConfig);
        postRepository = new PostRepository(dbConfig);
        userRepository.addUser(new UserCreateDao("testUser", "testPassword", "testName", "a@a.com"));
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    @Test
    void testAddAndGetPost() {
        // 게시물 추가
        User user = userRepository.getUserByUserId("testUser").get();
        PostCreateDao dao = new PostCreateDao(user.getId(), "Test Title", "Test Contents");
        postRepository.addPost(dao);

        // 게시물 조회
        Optional<Post> post = postRepository.getPost(1L);
        assertTrue(post.isPresent());
        assertEquals("Test Title", post.get().getTitle());
        assertEquals("Test Contents", post.get().getContents());
        assertEquals("testUser", post.get().getWriter().getUserId());
    }


    @Test
    void testGetPosts() {
        // 게시물 추가
        User user = userRepository.getUserByUserId("testUser").get();
        PostCreateDao dao1 = new PostCreateDao(user.getId(), "Test Title 1", "Test Contents 1");
        PostCreateDao dao2 = new PostCreateDao(user.getId(), "Test Title 2", "Test Contents 2");
        postRepository.addPost(dao1);
        postRepository.addPost(dao2);

        // 게시물 목록 조회
        List<Post> posts = postRepository.getPosts();
        assertEquals(2, posts.size());
    }

    @Test
    void testUpdatePost() {
        // 게시물 추가
        User user = userRepository.getUserByUserId("testUser").get();
        PostCreateDao dao = new PostCreateDao(user.getId(), "Test Title", "Test Contents");
        postRepository.addPost(dao);

        // 게시물 수정
        postRepository.updatePost(1L, "Updated Title", "Updated Contents");

        // 게시물 조회
        Optional<Post> post = postRepository.getPost(1L);
        assertTrue(post.isPresent());
        assertEquals("Updated Title", post.get().getTitle());
        assertEquals("Updated Contents", post.get().getContents());
    }

    @Test
    void testDeletePost() {
        // 게시물 추가
        User user = userRepository.getUserByUserId("testUser").get();
        PostCreateDao dao = new PostCreateDao(user.getId(), "Test Title", "Test Contents");
        postRepository.addPost(dao);

        // 게시물 삭제
        postRepository.deletePost(1L);

        // 게시물 조회
        List<Post> posts = postRepository.getPosts();
        assertFalse(posts.stream().anyMatch(post -> post.getId().equals(1L)));//isPresent
    }

    @Test
    void testGetNextPostPaged() {
        // 게시물 추가
        User user = userRepository.getUserByUserId("testUser").get();
        PostCreateDao dao1 = new PostCreateDao(user.getId(), "Test Title 1", "Test Contents 1");
        PostCreateDao dao2 = new PostCreateDao(user.getId(), "Test Title 2", "Test Contents 2");
        PostCreateDao dao3 = new PostCreateDao(user.getId(), "Test Title 3", "Test Contents 3");
        postRepository.addPost(dao1);
        postRepository.addPost(dao2);
        postRepository.addPost(dao3);

        // 첫 페이지 조회
        List<Post> posts = postRepository.getPostsPaged(1, 2);
        assertEquals(2, posts.size());
        assertEquals("Test Title 3", posts.get(0).getTitle());
        assertEquals("Test Title 2", posts.get(1).getTitle());

        // 두 번째 페이지 조회
        posts = postRepository.getPostsPaged(2, 2);
        assertEquals(1, posts.size());
        assertEquals("Test Title 1", posts.get(0).getTitle());
    }

    @Test
    void testGetPostsCount() {
        // 게시물 추가
        User user = userRepository.getUserByUserId("testUser").get();
        PostCreateDao dao1 = new PostCreateDao(user.getId(), "Test Title 1", "Test Contents 1");
        PostCreateDao dao2 = new PostCreateDao(user.getId(), "Test Title 2", "Test Contents 2");
        PostCreateDao dao3 = new PostCreateDao(user.getId(), "Test Title 3", "Test Contents 3");
        postRepository.addPost(dao1);
        postRepository.addPost(dao2);
        postRepository.addPost(dao3);

        assertEquals(3, postRepository.getTotalPostCount());
    }
}