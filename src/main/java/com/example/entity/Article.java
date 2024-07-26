package com.example.entity;

public class Article {

	private Long id;
	private String userId;
	private String title;
	private String contents;

	public Article(Long id, String userId, String title, String contents) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.contents = contents;
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

	public void updateId(Long id) {
		this.id = id;
	}
}
