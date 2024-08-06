package com.example.db;

import java.util.List;

import com.example.entity.Article;

public interface ArticleDatabase extends Database<Long, Article> {

	List<Article> findAllWithPagination(Long pageNumber);

	void delete(Long id);

	void updateUserName(String userId, String updateName);

	long getCount();
}
