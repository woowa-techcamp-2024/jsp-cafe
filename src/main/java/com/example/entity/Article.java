package com.example.entity;

import java.time.LocalDateTime;

public class Article {

	private Long id;
	private String userId;
	private String title;
	private String contents;
	private LocalDateTime createdAt;
	private boolean deleted;
	private String userName;

	public Article(Long id, String userId, String title, String contents, LocalDateTime createdAt, boolean deleted,
		String userName) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.contents = contents;
		this.createdAt = createdAt;
		this.deleted = deleted;
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void updateId(Long id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		deleted = true;
	}

	public String getUserName() {
		return userName;
	}
}
