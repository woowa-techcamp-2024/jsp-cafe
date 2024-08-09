package com.codesquad.cafe.model.aggregate;

import java.time.LocalDateTime;

import com.codesquad.cafe.db.domain.Post;
import com.codesquad.cafe.db.domain.User;
import com.codesquad.cafe.util.DateTimeFormatUtil;

public class PostWithAuthor {

	private final Long postId;

	private final String title;

	private final String content;

	private final String fileName;

	private int view;

	private final Long authorId;

	private final String authorUsername;

	private final LocalDateTime createdAt;

	private final LocalDateTime updatedAt;

	private final boolean deleted;

	public PostWithAuthor(Long postId,
		String title,
		String content,
		String fileName,
		int view,
		Long authorId,
		String authorUsername,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		boolean deleted) {
		this.postId = postId;
		this.title = title;
		this.content = content;
		this.fileName = fileName;
		this.view = view;
		this.authorId = authorId;
		this.authorUsername = authorUsername;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deleted = deleted;
	}

	public PostWithAuthor(Post post, User user) {
		this.postId = post.getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.fileName = post.getFileName();
		this.authorId = post.getAuthorId();
		this.authorUsername = user.getUsername();
		this.createdAt = post.getCreatedAt();
		this.updatedAt = post.getUpdatedAt();
		this.deleted = post.isDeleted();
	}

	public String getFormattedDate() {
		return DateTimeFormatUtil.getFormattedDate(createdAt);
	}

	public Long getPostId() {
		return postId;
	}

	public String getFileName() {
		return fileName;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public int getView() {
		return view;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public String getAuthorUsername() {
		return authorUsername;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public boolean isDeleted() {
		return deleted;
	}

}
