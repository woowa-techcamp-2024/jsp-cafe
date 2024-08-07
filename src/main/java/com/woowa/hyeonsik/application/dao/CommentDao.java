package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.Page;
import com.woowa.hyeonsik.application.domain.Reply;

import java.util.List;
import java.util.Optional;

public interface CommentDao {
    void save(Reply reply);
    Optional<Reply> findById(long id);
    boolean existsAnotherUser(long articleId, String userId);
    Page<Reply> findAllByArticleId(long articleId, long page);
    void update(Reply reply);
    void removeByReplyId(long replyId);
    void removeByArticleId(long articleId);
}
