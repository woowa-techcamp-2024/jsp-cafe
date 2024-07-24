package org.example.post.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class PostTest {

    @Test
    @DisplayName("유효한 데이터가 주어졌을 때 정상적으로 Post를 생성할 수 있다.")
    public void test_create_post_with_valid_values() {
        // Given
        String writer = "John Doe";
        String title = "Sample Title";
        String contents = "Sample Contents";

        // When
        Post post = Post.create(writer, title, contents);

        // Then
        assertNotNull(post);
        assertEquals(writer, post.getWriter());
        assertEquals(title, post.getTitle());
        assertEquals(contents, post.getContents());
    }

    @Test
    @DisplayName("id가 추가로 주어져도 정상적으로 생성할 수 있다.")
    public void test_create_post_with_id_writer_title_contents() {
        // Given
        Long id = 1L;
        String writer = "John Doe";
        String title = "Sample Title";
        String contents = "Sample Contents";

        // When
        Post post = Post.createWithId(id, writer, title, contents);

        // Then
        assertNotNull(post);
        assertEquals(id, post.getId());
        assertEquals(writer, post.getWriter());
        assertEquals(title, post.getTitle());
        assertEquals(contents, post.getContents());
    }

    @Test
    @DisplayName("getWriter가 호출되면 설정된 writer가 반환된다.")
    public void test_get_writer_of_post() {
        // Given
        String writer = "John Doe";
        Post post = Post.create(writer, "Sample Title", "Sample Contents");

        // When
        String result = post.getWriter();

        // Then
        assertEquals(writer, result);
    }

    @Test
    @DisplayName("getTitle이 호출되면 설정된 title이 반환된다.")
    public void test_get_title_of_post() {
        // Given
        String title = "Sample Title";
        Post post = Post.create("John Doe", title, "Sample Contents");

        // When
        String result = post.getTitle();

        // Then
        assertEquals(title, result);
    }

    @Test
    @DisplayName("getContents가 호출되면 설정된 contents가 반환된다.")
    public void test_get_contents_of_post() {
        // Given
        String contents = "Sample Contents";
        Post post = Post.create("John Doe", "Sample Title", contents);

        // When
        String result = post.getContents();

        // Then
        assertEquals(contents, result);
    }

    @Test
    @DisplayName("null이 필드로 주어지면 IllegalArgumentException 가 발생한다.")
    public void test_create_post_with_null_values() {
        // Given
        String writer = null;
        String title = null;
        String contents = null;

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            Post.create(writer, title, contents);
        });
    }

    @Test
    @DisplayName("빈 문자열이 필드로 주어지면 IllegalArgumentException 가 발생한다.")
    public void test_create_post_with_empty_string_values() {
        // Given
        String writer = "";
        String title = "";
        String contents = "";

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            Post.create(writer, title, contents);
        });
    }

    @Test
    @DisplayName("특수문자 문자열도 정상적으로 생성된다.")
    public void test_create_post_with_special_characters_values() {
        // Given
        String writer = "!@#$%^&*()";
        String title = "<>?/{}[]";
        String contents = "~`|\\\"'";

        // When
        Post post = Post.create(writer, title, contents);

        // Then
        assertNotNull(post);
        assertEquals(writer, post.getWriter());
        assertEquals(title, post.getTitle());
        assertEquals(contents, post.getContents());
    }

    @Test
    @DisplayName("id가 null 이어도 정상적으로 실행된다.")
    public void test_create_post_with_null_id() {
        // Given
        Long id = null;
        String writer = "John Doe";
        String title = "Sample Title";
        String contents = "Sample Contents";

        // When
        Post post = Post.createWithId(id, writer, title, contents);

        // Then
        assertNotNull(post);
        assertNull(post.getId());
        assertEquals(writer, post.getWriter());
        assertEquals(title, post.getTitle());
        assertEquals(contents, post.getContents());
    }

}