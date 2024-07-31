package com.woowa.hyeonsik.application.service;

import com.woowa.hyeonsik.application.dao.ArticleDao;
import com.woowa.hyeonsik.application.dao.CommentDao;
import com.woowa.hyeonsik.application.domain.Reply;

import com.woowa.hyeonsik.application.exception.AuthorizationException;
import java.util.List;

public class CommentService {
    private final CommentDao commentDao;
    private final ArticleDao articleDao;

    public CommentService(CommentDao commentDao, ArticleDao articleDao) {
        this.commentDao = commentDao;
        this.articleDao = articleDao;
    }

    // 어떤 게시글에 댓글을 작성한다.
    public void addComment(Reply reply) {
        validateArticle(reply.getArticleId());
        commentDao.save(reply);
    }

    // 어떤 게시글에 댓글을 모두 읽어온다.
    public List<Reply> findAllByArticleId(long articleId) {
        validateArticle(articleId);
        return commentDao.findAllByArticleId(articleId);
    }

    // 어떤 게시글에 댓글을 수정한다.
    public void updateComment(Reply reply, String userId) {
        validateOwnReply(reply.getId(), userId);
        commentDao.update(reply);
    }

    // 어떤 게시글에 댓글을 삭제한다.
    public void deleteComment(long replyId, String userId) {
        validateOwnReply(replyId, userId);
        commentDao.removeByReplyId(replyId);
    }

    // 게시글이 유효한지 검증
    private void validateArticle(long articleId) {
        if (!articleDao.findByArticleId(articleId).isPresent()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
    }

    // 댓글이 유효하고 접근 가능한지 검증
    private void validateOwnReply(long replyId, String userId) {
        final Reply reply = commentDao.findById(replyId)
            .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!reply.getWriter().equals(userId)) {
            throw new AuthorizationException("다른 사람이 쓴 댓글은 수정할 수 없습니다.");
        }
    }
}
