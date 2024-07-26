package com.example.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Article 엔티티 테스트")
class ArticleTest {

	@Test
	@DisplayName("아티클 객체 생성 및 필드 검증")
	void createArticle() {
		// given
		Long id = 1L;
		String writer = "writer";
		String title = "title";
		String contents = "contents";

		// when
		Article article = new Article(id, writer, title, contents);

		// then
		assertThat(article.getId()).isEqualTo(id);
		assertThat(article.getUserId()).isEqualTo(writer);
		assertThat(article.getTitle()).isEqualTo(title);
		assertThat(article.getContents()).isEqualTo(contents);
	}

	@Test
	@DisplayName("아티클 ID 업데이트")
	void updateArticleId() {
		// given
		Article article = new Article(null, "writer", "title", "contents");
		Long newId = 2L;

		// when
		article.updateId(newId);

		// then
		assertThat(article.getId()).isEqualTo(newId);
	}
}
