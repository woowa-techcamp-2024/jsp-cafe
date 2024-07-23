package com.example.entity;

public class Article {

	private Long id;
	private String writer;
	private String title;
	private String contents;

	public Article(Long id, String writer, String title, String contents) {
		this.id = id;
		this.writer = writer;
		this.title = title;
		this.contents = contents;
	}

	public Long getId() {
		return id;
	}

	public String getWriter() {
		return writer;
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
