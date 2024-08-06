package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.db.ArticleDatabase;
import com.example.db.ReplyDatabase;
import com.example.dto.SaveArticleRequest;
import com.example.dto.UpdateArticleRequest;
import com.example.entity.Article;
import com.example.entity.Reply;
import com.example.exception.BaseException;

public class ArticleService {

	private final ArticleDatabase articleDatabase;
	private final ReplyDatabase replyDatabase;

	public ArticleService(ArticleDatabase articleDatabase, ReplyDatabase replyDatabase) {
		this.articleDatabase = articleDatabase;
		this.replyDatabase = replyDatabase;
	}

	public void savePost(String userId, String userName, SaveArticleRequest request) {
		Article article = new Article(null, userId, request.title(), request.contents(), LocalDateTime.now(), false,
			userName);
		articleDatabase.insert(article);
	}

	public Article getArticle(Long articleId) {
		Optional<Article> articleOptional = articleDatabase.findById(articleId);
		if (articleOptional.isEmpty()) {
			throw BaseException.exception(404, "article not found");
		}
		return articleOptional.get();
	}

	public List<Article> getArticleByPage(Long pageNumber) {
		return articleDatabase.findAllWithPagination(pageNumber);
	}

	public void updateArticle(String userId, String userName, Long articleId, UpdateArticleRequest request) {
		checkValidation(userId, articleId);
		articleDatabase.update(articleId,
			new Article(null, null, request.title(), request.contents(), LocalDateTime.now(), false, userName));
	}

	//게시글 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태로 변경한다.
	// 댓글이 없는 경우 삭제가 가능하다.
	// 게시글 작성자와 댓글 작성자가 다를 경우 삭제는 불가능하다.
	// 단 게시글 작성자와 댓글 작성자가 모두 같은 경우 한 번에 삭제가 가능하다. 이 경우 게시글을 삭제할 때 댓글 또한 삭제해야 하며, 댓글의 삭제 또한 삭제 상태를 변경한다.
	public void deleteArticle(String userId, Long articleId) {
		checkValidation(userId, articleId);
		checkReplyValidation(userId, articleId);
		articleDatabase.delete(articleId);
		replyDatabase.deleteByArticleId(articleId);
	}

	private void checkReplyValidation(String userId, Long articleId) {
		List<Reply> replies = replyDatabase.findByArticleId(articleId);
		for (Reply reply : replies) {
			if (!reply.getUserId().equals(userId)) {
				throw BaseException.exception(400, "reply exists");
			}
		}
	}

	public void checkValidation(String userId, Long articleId) {
		Optional<Article> articleOptional = articleDatabase.findById(articleId);
		if (articleOptional.isEmpty()) {
			throw BaseException.exception(404, "article not found");
		}
		Article article = articleOptional.get();
		if (!article.getUserId().equals(userId)) {
			throw BaseException.exception(403, "not enough permissions");
		}
	}

	public long getTotalPages() {
		return articleDatabase.getCount();
	}
}
