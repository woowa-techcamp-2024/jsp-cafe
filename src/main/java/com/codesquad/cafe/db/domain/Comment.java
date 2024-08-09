package com.codesquad.cafe.db.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {

	private Long id;

	private Long postId;

	private Long parentId;

	private Long userId;

	private String content;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Boolean deleted;

	public Comment() {
	}

	public Comment(Long id, Long postId, Long parentId, Long userId, String content, LocalDateTime createdAt,
		LocalDateTime updatedAt, Boolean deleted) {
		if (postId == null || postId < 1) {
			throw new IllegalArgumentException("authorId는 1 이상의 값이어야 합니다.");
		}
		if (parentId != null && parentId < 1) {
			throw new IllegalArgumentException("parentId 1 이상의 값이어야 합니다.");
		}
		if (userId == null || userId < 1) {
			throw new IllegalArgumentException("userId 1 이상의 값이어야 합니다.");
		}
		if (content == null || content.isBlank()) {
			throw new IllegalArgumentException("content 필수 값입니다.");
		}
		if (createdAt == null || LocalDateTime.now().isBefore(createdAt)) {
			throw new IllegalArgumentException("createdAt 는 현재시간 이전이야 합니다.");
		}
		if (updatedAt == null || updatedAt.isBefore(createdAt)) {
			throw new IllegalArgumentException("updatedAt 은 createdAt 이후여야 합니다.");
		}
		this.id = id;
		this.postId = postId;
		this.parentId = parentId;
		this.userId = userId;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deleted = deleted;
	}

	public static Comment of(Long postId, Long parentId, Long userId, String content) {
		LocalDateTime now = LocalDateTime.now();
		return new Comment(null, postId, parentId, userId, content, now, now, false);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPostId() {
		return postId;
	}

	public Long getParentId() {
		return parentId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getContent() {
		return content;
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

	public void update(String content) {
		if (content == null || content.isEmpty()) {
			throw new IllegalArgumentException("content is required");
		}
		this.content = content;
		this.updatedAt = LocalDateTime.now();
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
		Comment comment = (Comment)o;
		return Objects.equals(id, comment.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		String sb = "Comment{" + "id=" + id
			+ ", postId=" + postId
			+ ", parentId=" + parentId
			+ ", userId=" + userId
			+ ", content='" + content + '\''
			+ ", createdAt=" + createdAt
			+ ", updatedAt=" + updatedAt
			+ ", deleted=" + deleted
			+ '}';
		return sb;
	}

}
