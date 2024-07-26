package com.example.service;

import java.util.Optional;

import com.example.db.ArticleDatabase;
import com.example.dto.SaveArticleRequest;
import com.example.entity.Article;

public class ArticleService {

	private final ArticleDatabase articleDatabase;

	public ArticleService(ArticleDatabase articleDatabase) {
		this.articleDatabase = articleDatabase;
	}

	public void savePost(String userId, SaveArticleRequest request) {
		Article article = new Article(null, userId, request.title(), request.contents());
		articleDatabase.insert(article);
	}

	public Article getArticle(Long articleId) {
		Optional<Article> articleOptional = articleDatabase.findById(articleId);
		if (articleOptional.isEmpty()) {
			throw new RuntimeException("article not found");
		}
		return articleOptional.get();
	}
}
