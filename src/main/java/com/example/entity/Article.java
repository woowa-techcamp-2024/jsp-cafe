package com.example.entity;

import java.time.LocalDateTime;

public class Article {

	private Long id;
	private String userId;
	private String title;
	private String contents;
	private LocalDateTime createdAt;

	public Article(Long id, String userId, String title, String contents, LocalDateTime createdAt) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.contents = contents;
		this.createdAt = createdAt;
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
}
