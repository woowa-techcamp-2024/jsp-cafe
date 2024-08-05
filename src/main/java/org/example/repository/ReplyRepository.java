package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.Reply;

public interface ReplyRepository {
    void save(Reply reply);
    void deleteById(int replyId);
    List<Reply> findAllByArticleId(int articleId, int start, int count);
    void deleteAllByArticleId(int articleId);
    Optional<Reply> findById(int replyId);

    int findReplyCount(Integer articleId);

    List<Reply> findRealAll(int articleId);
}
