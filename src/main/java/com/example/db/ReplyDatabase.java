package com.example.db;

import java.time.LocalDateTime;
import java.util.List;

import com.example.entity.Reply;

public interface ReplyDatabase extends Database<Long, Reply> {

	void delete(Long id);

	long countByArticleId(Long articleId);

	List<Reply> findByArticleId(Long articleId);

	void deleteByArticleId(Long articleId);

	void updateUserName(String id, String updateName);

	List<Reply> findByArticleIdWithPagination(Long articleId, Long lastReplyId, LocalDateTime lastCreatedAt);
}
