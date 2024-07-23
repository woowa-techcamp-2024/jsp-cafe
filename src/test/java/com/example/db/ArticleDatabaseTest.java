package com.example.db;

import com.example.entity.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArticleDatabase 테스트")
class ArticleDatabaseTest {

	@Test
	@DisplayName("아티클을 데이터베이스에 추가할 수 있다")
	void insertArticle() {
		ArticleDatabase articleDatabase = new ArticleDatabase();
		Article article = new Article(null, "writer", "title", "contents");

		articleDatabase.insert(article);

		Optional<Article> foundArticle = articleDatabase.findById(article.getId());
		assertThat(foundArticle).isPresent();
		assertThat(foundArticle.get().getWriter()).isEqualTo("writer");
		assertThat(foundArticle.get().getTitle()).isEqualTo("title");
		assertThat(foundArticle.get().getContents()).isEqualTo("contents");
	}

	@Test
	@DisplayName("모든 아티클을 조회할 수 있다")
	void findAllArticles() {
		ArticleDatabase articleDatabase = new ArticleDatabase();
		Article article1 = new Article(null, "writer1", "title1", "contents1");
		Article article2 = new Article(null, "writer2", "title2", "contents2");
		articleDatabase.insert(article1);
		articleDatabase.insert(article2);

		List<Article> articles = articleDatabase.findAll();

		assertThat(articles).hasSize(2).containsExactlyInAnyOrder(article1, article2);
	}

	@Test
	@DisplayName("아티클 아이디로 아티클을 조회할 수 있다")
	void findArticleById() {
		ArticleDatabase articleDatabase = new ArticleDatabase();
		Article article1 = new Article(null, "writer1", "title1", "contents1");
		Article article2 = new Article(null, "writer2", "title2", "contents2");
		articleDatabase.insert(article1);
		articleDatabase.insert(article2);

		Optional<Article> foundArticle = articleDatabase.findById(article1.getId());

		assertThat(foundArticle).isPresent();
		assertThat(foundArticle.get().getWriter()).isEqualTo("writer1");
	}

	@Test
	@DisplayName("존재하지 않는 아티클 아이디로 아티클을 조회하면 빈 값을 반환한다")
	void findArticleByIdNotFound() {
		ArticleDatabase articleDatabase = new ArticleDatabase();

		Optional<Article> foundArticle = articleDatabase.findById(999L);

		assertThat(foundArticle).isNotPresent();
	}

	@Test
	@DisplayName("아티클을 업데이트할 수 있다")
	void updateArticle() {
		ArticleDatabase articleDatabase = new ArticleDatabase();
		Article article = new Article(null, "writer", "title", "contents");
		articleDatabase.insert(article);

		Article updatedArticle = new Article(article.getId(), "newWriter", "newTitle", "newContents");
		articleDatabase.update(article.getId(), updatedArticle);

		Optional<Article> foundArticle = articleDatabase.findById(article.getId());
		assertThat(foundArticle).isPresent();
		assertThat(foundArticle.get().getWriter()).isEqualTo("newWriter");
		assertThat(foundArticle.get().getTitle()).isEqualTo("newTitle");
		assertThat(foundArticle.get().getContents()).isEqualTo("newContents");
	}
}
