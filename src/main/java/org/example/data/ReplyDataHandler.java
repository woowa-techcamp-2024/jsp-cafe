package org.example.data;

import java.util.List;
import org.example.domain.Reply;

public interface ReplyDataHandler {
    Reply insert(Reply reply);

    Reply update(Reply reply);

    Reply findByReplyId(Long replyId);

    List<Reply> findAllByArticleId(Long articleId);

    void deleteAllByArticleId(Long articleId);
}
