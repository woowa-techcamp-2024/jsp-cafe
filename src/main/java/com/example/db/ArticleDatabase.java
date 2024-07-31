package com.example.db;

import com.example.entity.Article;

public interface ArticleDatabase extends Database<Long, Article> {

	void delete(Long id);

	void updateUserName(String userId, String updateName);
}
