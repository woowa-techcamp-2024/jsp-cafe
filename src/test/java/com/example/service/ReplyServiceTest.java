package com.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.db.ReplyDatabase;
import com.example.entity.Reply;
import com.example.exception.BaseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReplyServiceTest {

	private ReplyService replyService;
	private ReplyDatabase replyDatabase;

	@BeforeEach
	void setUp() {
		replyDatabase = mock(ReplyDatabase.class);
		replyService = new ReplyService(replyDatabase);
	}

	@Test
	void getReplyCount() {
		// given
		Long articleId = 1L;
		when(replyDatabase.countByArticleId(articleId)).thenReturn(5L);

		// when
		long count = replyService.getReplyCount(articleId);

		// then
		assertThat(count).isEqualTo(5L);
	}

	@Test
	void findAll() {
		// given
		Long articleId = 1L;
		Reply reply = new Reply(1L, "content", LocalDateTime.now(), false, articleId, "userId", "userName");
		when(replyDatabase.findByArticleId(articleId)).thenReturn(List.of(reply));

		// when
		List<Reply> replies = replyService.findAll(articleId);

		// then
		assertThat(replies).hasSize(1);
		assertThat(replies.get(0).getContents()).isEqualTo("content");
	}

	@Test
	void addReply() {
		// given
		Reply reply = new Reply(null, "content", LocalDateTime.now(), false, 1L, "userId", "userName");
		when(replyDatabase.insert(reply)).thenReturn(1L);

		// when
		Long replyId = replyService.addReply(reply);

		// then
		assertThat(replyId).isEqualTo(1L);
	}

	@Test
	void deleteReply_notFound() {
		// given
		Long replyId = 1L;
		String userId = "userId";
		when(replyDatabase.findById(replyId)).thenReturn(Optional.empty());

		// when
		BaseException exception = null;
		try {
			replyService.deleteReply(replyId, userId);
		} catch (BaseException e) {
			exception = e;
		}

		// then
		assertThat(exception).isNotNull();
		assertThat(exception.getStatus()).isEqualTo(404);
	}

	@Test
	void deleteReply_notPermitted() {
		// given
		Long replyId = 1L;
		String userId = "userId";
		Reply reply = new Reply(replyId, "content", LocalDateTime.now(), false, 1L, "otherUserId", "userName");
		when(replyDatabase.findById(replyId)).thenReturn(Optional.of(reply));

		// when
		BaseException exception = null;
		try {
			replyService.deleteReply(replyId, userId);
		} catch (BaseException e) {
			exception = e;
		}

		// then
		assertThat(exception).isNotNull();
		assertThat(exception.getStatus()).isEqualTo(403);
	}

	@Test
	void deleteReply_success() {
		// given
		Long replyId = 1L;
		String userId = "userId";
		Reply reply = new Reply(replyId, "content", LocalDateTime.now(), false, 1L, userId, "userName");
		when(replyDatabase.findById(replyId)).thenReturn(Optional.of(reply));

		// when
		replyService.deleteReply(replyId, userId);

		// then
		verify(replyDatabase).delete(replyId);
	}
}
