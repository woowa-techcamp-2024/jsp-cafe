package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.Reply;

import java.util.List;
import java.util.Optional;

public interface CommentDao {
    void save(Reply reply);
    Optional<Reply> findById(long id);
    List<Reply> findAllByArticleId(long articleId);
    void update(Reply reply);
    void removeByReplyId(long replyId);
}
