package com.example.entity;

import java.time.LocalDateTime;

public class Reply {

	private Long id;
	private String contents;
	private LocalDateTime createdAt;
	private boolean deleted;
	private Long articleId;
	private String userId;
	private String userName;

	public Reply(Long id, String contents, LocalDateTime createdAt, boolean deleted, Long articleId, String userId,
		String userName) {
		this.id = id;
		this.contents = contents;
		this.createdAt = createdAt;
		this.deleted = deleted;
		this.articleId = articleId;
		this.userId = userId;
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public String getContents() {
		return contents;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public Long getArticleId() {
		return articleId;
	}

	public String getUserId() {
		return userId;
	}

	public void delete() {
		deleted = true;
	}

	public String getUserName() {
		return userName;
	}
}
