package com.example.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.example.entity.Article;

public class ArticleMemoryDatabase implements ArticleDatabase {

	private final Map<Long, Article> articles = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(0);

	@Override
	public Long insert(Article article) {
		Long id = getNextId();
		article.updateId(id);
		articles.put(id, article);
		return id;
	}

	@Override
	public long getCount() {
		return 0;
	}

	@Override
	public Optional<Article> findById(Long id) {
		return Optional.ofNullable(articles.get(id));
	}

	@Override
	public List<Article> findAll() {
		return new ArrayList<>(articles.values());
	}

	private long getNextId() {
		return idGenerator.incrementAndGet();
	}

	@Override
	public void update(Long aLong, Article article) {
		articles.put(aLong, article);
	}

	@Override
	public List<Article> findAllWithPagination(Long pageNumber) {
		return List.of();
	}

	@Override
	public void delete(Long id) {
		articles.remove(id);
	}

	@Override
	public void updateUserName(String userId, String updateName) {
	}
}
