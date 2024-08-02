package com.codesquad.cafe.db.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    @DisplayName("객체 생성")
    void testPostCreation() {
        LocalDateTime now = LocalDateTime.now();
        Post post = new Post(1L, 1L, "title", "content", "fileName", 0, now, now, false);

        assertEquals(1L, post.getId());
        assertEquals(1L, post.getAuthorId());
        assertEquals("title", post.getTitle());
        assertEquals("content", post.getContent());
        assertEquals("fileName", post.getFileName());
        assertEquals(0, post.getView());
        assertEquals(now, post.getCreatedAt());
        assertEquals(now, post.getUpdatedAt());
        assertFalse(post.isDeleted());
    }

    @Test
    @DisplayName("Post.of 메서드")
    void testPostOfMethod() {
        Post post = Post.of(1L, "title", "content", "fileName");

        assertNull(post.getId());
        assertEquals(1L, post.getAuthorId());
        assertEquals("title", post.getTitle());
        assertEquals("content", post.getContent());
        assertEquals("fileName", post.getFileName());
        assertEquals(0, post.getView());
        assertNotNull(post.getCreatedAt());
        assertNotNull(post.getUpdatedAt());
        assertFalse(post.isDeleted());
    }

    @Test
    @DisplayName("생성자 유효성 검사")
    void testPostConstructorValidation() {
        LocalDateTime now = LocalDateTime.now();

        assertThrows(IllegalArgumentException.class,
                () -> new Post(1L, 0L, "title", "content", "fileName", 0, now, now, false));
        assertThrows(IllegalArgumentException.class,
                () -> new Post(1L, 1L, "", "content", "fileName", 0, now, now, false));
        assertThrows(IllegalArgumentException.class,
                () -> new Post(1L, 1L, "title", "", "fileName", 0, now, now, false));
        assertThrows(IllegalArgumentException.class,
                () -> new Post(1L, 1L, "title", "content", "fileName", -1, now, now, false));
    }

    @Test
    @DisplayName("조회수 증가")
    void testAddView() {
        Post post = Post.of(1L, "title", "content", "fileName");
        post.addView();
        assertEquals(1, post.getView());
    }

    @Test
    @DisplayName("삭제 메서드")
    void testDelete() {
        User user = User.of("username", "password", "name", "email@example.com");
        user.delete();

        assertTrue(user.isDeleted());
        assertTrue(user.getUpdatedAt().isAfter(user.getCreatedAt()));
    }

    @Test
    @DisplayName("equals - id 없는 객체")
    void testEqualsDifferentCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        Post firstPost = new Post(null, 1L, "title", "content", null, 100, now, now, false);
        Post secondPost = new Post(null, 1L, "title", "content", null, 100, now, now, false);

        assertEquals(firstPost, secondPost);
    }

    @Test
    @DisplayName("equals -  id 있는 객체")
    void testEqualsWitId() {
        LocalDateTime now = LocalDateTime.now();
        Post firstPost = new Post(100L, 1L, "title", "content", null, 100, now, now, false);
        Post secondPost = new Post(100L, 1L, "title", "content", null, 100, now, now, false);

        assertEquals(firstPost, secondPost);
    }
}
