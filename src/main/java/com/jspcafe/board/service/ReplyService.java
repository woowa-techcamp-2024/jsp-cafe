package com.jspcafe.board.service;

import com.jspcafe.board.model.Reply;
import com.jspcafe.board.model.ReplyDao;
import com.jspcafe.exception.ReplyNotFoundException;

import java.util.List;

public class ReplyService {
    private final ReplyDao replyDao;

    public ReplyService(final ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    public List<Reply> findByArticleId(final String articleId) {
        return replyDao.findByArticleId(articleId);
    }

    public Reply findById(final String id) throws ReplyNotFoundException {
        return replyDao.findById(id)
                .orElseThrow(() -> new ReplyNotFoundException("Reply id not found, id = " + id));
    }

    public Reply save(final String articleId, final String id, final String nickname, final String content) {
        Reply reply = Reply.create(articleId, id, nickname, content);
        replyDao.save(reply);
        return reply;
    }

    public Reply update(final String id, final String content) {
        Reply reply = findById(id);
        Reply updateReply = reply.update(content);
        replyDao.update(updateReply);
        return updateReply;
    }

    public boolean delete(final String id) {
        replyDao.softDelete(id);
        return true;
    }
}
