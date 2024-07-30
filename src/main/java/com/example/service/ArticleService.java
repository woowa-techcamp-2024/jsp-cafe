package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.db.ArticleDatabase;
import com.example.dto.SaveArticleRequest;
import com.example.dto.UpdateArticleRequest;
import com.example.entity.Article;
import com.example.exception.BaseException;

public class ArticleService {

	private final ArticleDatabase articleDatabase;

	public ArticleService(ArticleDatabase articleDatabase) {
		this.articleDatabase = articleDatabase;
	}

	public void savePost(String userId, SaveArticleRequest request) {
		Article article = new Article(null, userId, request.title(), request.contents(), LocalDateTime.now());
		articleDatabase.insert(article);
	}

	public Article getArticle(Long articleId) {
		Optional<Article> articleOptional = articleDatabase.findById(articleId);
		if (articleOptional.isEmpty()) {
			throw BaseException.exception(404, "article not found");
		}
		return articleOptional.get();
	}

	public void updateArticle(String userId, Long articleId, UpdateArticleRequest request) {
		checkValidation(userId, articleId);
		articleDatabase.update(articleId,
			new Article(null, null, request.title(), request.contents(), LocalDateTime.now()));
	}

	public void deleteArticle(String userId, Long articleId) {
		checkValidation(userId, articleId);
		articleDatabase.delete(articleId);
	}

	public void checkValidation(String userId, Long articleId) {
		Optional<Article> articleOptional = articleDatabase.findById(articleId);
		if (articleOptional.isEmpty()) {
			throw BaseException.exception(404, "article not found");
		}
		Article article = articleOptional.get();
		if (!article.getUserId().equals(userId)) {
			throw BaseException.exception(403, "not enough permissions");
		}
	}
}
