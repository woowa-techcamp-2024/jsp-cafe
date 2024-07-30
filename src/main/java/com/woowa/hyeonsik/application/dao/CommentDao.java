package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.Reply;

import java.util.List;

public interface CommentDao {
    void save(Reply reply);
    List<Reply> findAllByArticleId(long articleId);
}
