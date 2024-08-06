package com.hyeonuk.jspcafe.reply.dao;

import com.hyeonuk.jspcafe.reply.domain.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyDao {
    Reply save(Reply reply);
    void deleteById(Long id);
    void deleteAllByArticleId(Long articleId);
    Optional<Reply> findById(Long id);
    List<Reply> findAllByArticleId(Long articleId);
    long count();
}
