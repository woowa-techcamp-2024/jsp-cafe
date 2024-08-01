package org.example.demo.repository;

import org.example.demo.domain.Comment;
import org.example.demo.domain.Post;
import org.example.demo.domain.User;
import org.example.demo.model.CommentCreateDao;
import org.example.demo.model.PostCreateDao;
import org.example.demo.model.UserCreateDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentRepositoryTest extends RepositoryTestSupport {
    UserRepository userRepository;
    PostRepository postRepository;
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        setDb();
        userRepository = new UserRepository(dbConfig);
        postRepository = new PostRepository(dbConfig);
        commentRepository = new CommentRepository(dbConfig);
        userRepository.addUser(new UserCreateDao("testUser", "testPassword", "testName", "a@a.com"));
        postRepository.addPost(new PostCreateDao(1L, "Test Contents", "testUser"));
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    @Test
    void testSaveAndGetComment() {
        // 댓글 추가
        User user = userRepository.getUserByUserId("testUser").get();
        CommentCreateDao commentCreateDao = new CommentCreateDao(1L, user.getId(), "Test Comment Contents");
        commentRepository.saveComment(commentCreateDao);

        // 댓글 조회
        Optional<Comment> comment = commentRepository.getComment(1L);
        assertTrue(comment.isPresent());
        assertEquals("Test Comment Contents", comment.get().getContents());
        assertEquals("testUser", comment.get().getWriter().getUserId());
    }

    @Test
    void testGetComments() {
        // 댓글 추가
        User user = userRepository.getUserByUserId("testUser").get();
        commentRepository.saveComment(new CommentCreateDao(1L, user.getId(), "Test Comment Contents 1"));
        commentRepository.saveComment(new CommentCreateDao(1L, user.getId(), "Test Comment Contents 2"));

        // 댓글 목록 조회
        List<Comment> comments = commentRepository.getComments(1L);
        assertEquals(2, comments.size());
    }

    @Test
    void testUpdateComment() {
        // 댓글 추가
        User user = userRepository.getUserByUserId("testUser").get();
        commentRepository.saveComment(new CommentCreateDao(1L, user.getId(), "Test Comment Contents"));

        // 댓글 수정
        commentRepository.updateComment(1L, "Updated Comment Contents");

        // 댓글 조회
        Optional<Comment> comment = commentRepository.getComment(1L);
        assertTrue(comment.isPresent());
        assertEquals("Updated Comment Contents", comment.get().getContents());
    }

    @Test
    void testDeleteComment() {
        // 댓글 추가
        User user = userRepository.getUserByUserId("testUser").get();
        commentRepository.saveComment(new CommentCreateDao(1L, user.getId(), "Test Comment Contents"));

        // 댓글 삭제
        commentRepository.deleteComment(1L);

        // 댓글 조회
        Post post = postRepository.getPost(1L).get();
        List<Comment> comments = post.getComments();

        assertEquals(0, comments.size());
    }

}