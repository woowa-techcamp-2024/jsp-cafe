package org.example.mock;

import java.util.List;
import org.example.data.ReplyDataHandler;
import org.example.domain.Reply;

public class TestReplyDataHandler implements ReplyDataHandler {
    @Override
    public Reply insert(Reply reply) {
        return null;
    }

    @Override
    public Reply update(Reply reply) {
        return null;
    }

    @Override
    public Reply findByReplyId(Long replyId) {
        return null;
    }

    @Override
    public List<Reply> findAllByArticleId(Long articleId) {
        return List.of();
    }

    @Override
    public void deleteAllByArticleId(Long articleId) {

    }
}
