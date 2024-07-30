package com.woowa.hyeonsik.application.service;

import com.woowa.hyeonsik.application.dao.CommentDao;
import com.woowa.hyeonsik.application.domain.Reply;

import java.util.List;

public class CommentService {
    private final CommentDao commentDao;

    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    // 어떤 게시글에 댓글을 작성한다.
    public void addComment(Reply reply) {
        // FIXME 유효한 게시글인지 확인
        commentDao.save(reply);
    }

    // 어떤 게시글에 댓글을 모두 읽어온다.
    public List<Reply> findAllByArticleId(long articleId) {
        // FIXME 유효한 게시글인지 확인
        return commentDao.findAllByArticleId(articleId);
    }

    // TODO: 어떤 게시글에 댓글을 수정한다.
    public void updateComment(Reply reply, String userId) {
        throw new UnsupportedOperationException();
    }

    // TODO: 어떤 게시글에 댓글을 삭제한다.
    public void deleteComment(long replyId, String userId) {
        throw new UnsupportedOperationException();
    }
}
