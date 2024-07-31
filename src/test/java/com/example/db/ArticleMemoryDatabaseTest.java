package com.example.db;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.entity.Article;

@DisplayName("ArticleDatabase 테스트")
class ArticleMemoryDatabaseTest {

	@Test
	@DisplayName("아티클을 데이터베이스에 추가할 수 있다")
	void insertArticle() {
		ArticleMemoryDatabase articleMemoryDatabase = new ArticleMemoryDatabase();
		Article article = new Article(null, "writer", "title", "contents", LocalDateTime.now(), false, "username");

		articleMemoryDatabase.insert(article);

		Optional<Article> foundArticle = articleMemoryDatabase.findById(article.getId());
		assertThat(foundArticle).isPresent();
		assertThat(foundArticle.get().getUserId()).isEqualTo("writer");
		assertThat(foundArticle.get().getTitle()).isEqualTo("title");
		assertThat(foundArticle.get().getContents()).isEqualTo("contents");
	}

	@Test
	@DisplayName("모든 아티클을 조회할 수 있다")
	void findAllArticles() {
		ArticleMemoryDatabase articleMemoryDatabase = new ArticleMemoryDatabase();
		Article article1 = new Article(null, "writer1", "title1", "contents1", LocalDateTime.now(), false, "username1");
		Article article2 = new Article(null, "writer2", "title2", "contents2", LocalDateTime.now(), false, "username2");
		articleMemoryDatabase.insert(article1);
		articleMemoryDatabase.insert(article2);

		List<Article> articles = articleMemoryDatabase.findAll();

		assertThat(articles).hasSize(2).containsExactlyInAnyOrder(article1, article2);
	}

	@Test
	@DisplayName("아티클 아이디로 아티클을 조회할 수 있다")
	void findArticleById() {
		ArticleMemoryDatabase articleMemoryDatabase = new ArticleMemoryDatabase();
		Article article1 = new Article(null, "writer1", "title1", "contents1", LocalDateTime.now(), false, "username1");
		Article article2 = new Article(null, "writer2", "title2", "contents2", LocalDateTime.now(), false, "username2");
		articleMemoryDatabase.insert(article1);
		articleMemoryDatabase.insert(article2);

		Optional<Article> foundArticle = articleMemoryDatabase.findById(article1.getId());

		assertThat(foundArticle).isPresent();
		assertThat(foundArticle.get().getUserId()).isEqualTo("writer1");
	}

	@Test
	@DisplayName("존재하지 않는 아티클 아이디로 아티클을 조회하면 빈 값을 반환한다")
	void findArticleByIdNotFound() {
		ArticleMemoryDatabase articleMemoryDatabase = new ArticleMemoryDatabase();

		Optional<Article> foundArticle = articleMemoryDatabase.findById(999L);

		assertThat(foundArticle).isNotPresent();
	}

	@Test
	@DisplayName("아티클을 업데이트할 수 있다")
	void updateArticle() {
		ArticleMemoryDatabase articleMemoryDatabase = new ArticleMemoryDatabase();
		Article article = new Article(null, "writer", "title", "contents", LocalDateTime.now(), false, "username");
		articleMemoryDatabase.insert(article);

		Article updatedArticle = new Article(article.getId(), "newWriter", "newTitle", "newContents",
			LocalDateTime.now(), false, "username");
		articleMemoryDatabase.update(article.getId(), updatedArticle);

		Optional<Article> foundArticle = articleMemoryDatabase.findById(article.getId());
		assertThat(foundArticle).isPresent();
		assertThat(foundArticle.get().getUserId()).isEqualTo("newWriter");
		assertThat(foundArticle.get().getTitle()).isEqualTo("newTitle");
		assertThat(foundArticle.get().getContents()).isEqualTo("newContents");
	}
}
