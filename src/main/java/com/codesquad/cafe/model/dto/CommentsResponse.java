package com.codesquad.cafe.model.dto;

import java.util.List;

import com.codesquad.cafe.model.aggregate.CommentWithUser;

public class CommentsResponse {

	private List<CommentWithUser> comments;

	private Long lastCommentId;

	public CommentsResponse() {
	}

	public CommentsResponse(List<CommentWithUser> comments) {
		this.comments = comments;
		if (!comments.isEmpty()) {
			this.lastCommentId = comments.get(comments.size() - 1).getId();
		} else {
			this.lastCommentId = null;
		}
	}

	public List<CommentWithUser> getComments() {
		return comments;
	}

	public Long getLastCommentId() {
		return lastCommentId;
	}

}
