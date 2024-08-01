package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.db.ReplyDatabase;
import com.example.entity.Reply;
import com.example.exception.BaseException;

public class ReplyService {

	private final ReplyDatabase replyDatabase;

	public ReplyService(ReplyDatabase replyDatabase) {
		this.replyDatabase = replyDatabase;
	}

	public long getReplyCount(Long articleId) {
		return replyDatabase.countByArticleId(articleId);
	}

	public List<Reply> findAll(Long articleId) {
		return replyDatabase.findByArticleId(articleId);
	}

	public Long addReply(Reply reply) {
		return replyDatabase.insert(reply);
	}

	public void deleteReply(long replyId, String userId) {
		Optional<Reply> replyOptional = replyDatabase.findById(replyId);
		if (replyOptional.isEmpty()) {
			throw BaseException.exception(404, "reply not found");
		}
		Reply reply = replyOptional.get();
		if (!reply.getUserId().equals(userId)) {
			throw BaseException.exception(403, "not enough permissions");
		}

		replyDatabase.delete(replyId);
	}
}
