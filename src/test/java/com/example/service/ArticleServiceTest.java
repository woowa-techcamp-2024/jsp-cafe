package com.example.service;

import com.example.db.ArticleDatabase;
import com.example.dto.SaveArticleRequest;
import com.example.entity.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("ArticleService 테스트")
class ArticleServiceTest {

	private ArticleService articleService;
	private ArticleDatabase articleDatabase;

	@BeforeEach
	void setUp() {
		articleDatabase = mock(ArticleDatabase.class);
		articleService = new ArticleService(articleDatabase);
	}

	@Test
	@DisplayName("게시글을 저장할 수 있다")
	void savePost() {
		// given
		String userId = "user1";
		SaveArticleRequest request = new SaveArticleRequest("title", "contents");

		// when
		articleService.savePost(userId, request);

		// then
		verify(articleDatabase, times(1)).insert(any(Article.class));
	}

	@Test
	@DisplayName("게시글을 조회할 수 있다")
	void getArticle() {
		// given
		Long articleId = 1L;
		Article article = new Article(articleId, "user1", "title", "contents", LocalDateTime.now());
		when(articleDatabase.findById(articleId)).thenReturn(Optional.of(article));

		// when
		Article foundArticle = articleService.getArticle(articleId);

		// then
		assertThat(foundArticle).isEqualTo(article);
	}

	@Test
	@DisplayName("존재하지 않는 게시글 조회 시 예외를 던진다")
	void getArticleNotFound() {
		// given
		Long articleId = 1L;
		when(articleDatabase.findById(articleId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> articleService.getArticle(articleId))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("article not found");
	}
}
