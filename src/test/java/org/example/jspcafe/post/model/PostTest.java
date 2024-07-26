package org.example.jspcafe.post.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostTest {

    @DisplayName("Post를 생성할 수 있다.")
    @Test
    void createPost() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0, 0);

        // when
        Post post = new Post(userId, title, content, createdAt);

        // then
        assertThat(post)
                .extracting("userId", "title.value", "content.value", "createdAt")
                .containsExactly(userId, title, content, createdAt);
    }

    @DisplayName("createdAt이 null이면 예외가 발생한다.")
    @Test
    void createPostWithNullCreatedAt() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        LocalDateTime createdAt = null;

        // when & then
        assertThatThrownBy(() -> new Post(userId, title, content, createdAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("생성일이 없습니다.");
    }

    @DisplayName("Post의 Title을 수정할 수 있다.")
    @Test
    void updateTitleOfPost() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0, 0);

        Post post = new Post(userId, title, content, createdAt);

        String newTitle = "newTitle";

        // when
        post.updateTitle(newTitle);

        // then
        assertThat(post)
                .extracting("userId", "title.value", "content.value", "createdAt")
                .containsExactly(userId, newTitle, content, createdAt);
    }

    @DisplayName("Post의 Content를 수정할 수 있다.")
    @Test
    void updateContentOfPost() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0, 0);

        Post post = new Post(userId, title, content, createdAt);

        String newContent = "newContent";

        // when
        post.updateContent(newContent);

        // then
        assertThat(post)
                .extracting("userId", "title.value", "content.value", "createdAt")
                .containsExactly(userId, title, newContent, createdAt);
    }

}