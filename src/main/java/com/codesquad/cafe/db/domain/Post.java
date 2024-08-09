package com.codesquad.cafe.db.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

	private Long id;

	private Long authorId;

	private String title;

	private String content;

	private String fileName;

	private Integer view;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Boolean deleted;

	public Post() {
	}

	public Post(Long id, Long authorId, String title, String content, String fileName, Integer view,
		LocalDateTime createdAt, LocalDateTime updatedAt, Boolean deleted) {
		if (authorId == null || authorId < 1) {
			throw new IllegalArgumentException("authorId는 1 이상의 값이어야 합니다.");
		}
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("title 필수 값입니다.");
		}
		if (content == null || content.isEmpty()) {
			throw new IllegalArgumentException("content 필수 값입니다.");
		}
		if (view < 0) {
			throw new IllegalArgumentException("view 는 0이상이어야 합니다.");
		}
		if (createdAt == null || LocalDateTime.now().isBefore(createdAt)) {
			throw new IllegalArgumentException("createdAt 는 현재시간 이전이야 합니다.");
		}
		if (updatedAt == null || updatedAt.isBefore(createdAt)) {
			throw new IllegalArgumentException("updatedAt 은 createdAt 이후여야 합니다.");
		}
		this.id = id;
		this.authorId = authorId;
		this.title = title;
		this.content = content;
		this.fileName = fileName;
		this.view = view;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deleted = deleted;
	}

	public static Post of(Long authorId, String title, String content, String fileName) {
		LocalDateTime now = LocalDateTime.now();
		return new Post(null, authorId, title, content, fileName, 0, now, now, false);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getFileName() {
		return fileName;
	}

	public int getView() {
		return view;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void update(String title, String content, String fileName) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("title is required");
		}
		if (content == null || content.isEmpty()) {
			throw new IllegalArgumentException("content is required");
		}
		this.content = content;
		this.title = title;
		this.fileName = fileName;
		this.updatedAt = LocalDateTime.now();
	}

	public void addView() {
		this.view++;
	}

	public void delete() {
		this.deleted = true;
		this.updatedAt = LocalDateTime.now();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Post post = (Post)o;
		return Objects.equals(id, post.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		String sb = "Post{" + "id=" + id
			+ ", userId=" + authorId
			+ ", title='" + title + '\''
			+ ", content='" + content + '\''
			+ ", filename='" + fileName + '\''
			+ ", view='" + view + '\''
			+ ", createdAt=" + createdAt
			+ ", updatedAt=" + updatedAt
			+ (deleted ? ", deleted" : "")
			+ '}';
		return sb;
	}

}
