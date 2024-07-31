package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {

    Long save(final Reply reply);

    Optional<Reply> findById(final Long replyId);

    List<Reply> findByArticleId(final Long articleId);

    List<Reply> findAll();

    Optional<Reply> update(final Reply reply);

    void delete(final Long replyId);

    void deleteByArticleId(final Long articleId);
}
