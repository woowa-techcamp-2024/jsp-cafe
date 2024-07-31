package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Reply;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryReplyRepository implements ReplyRepository {

    public AtomicLong replyId = new AtomicLong(1);
    private final Map<Long, Reply> replies = new ConcurrentHashMap<>();

    @Override
    public Long save(final Reply reply) {
        final Long newReplyId = replyId.getAndIncrement();
        reply.setId(newReplyId);

        replies.put(newReplyId, reply);

        return newReplyId;
    }

    @Override
    public Optional<Reply> findById(final Long replyId) {
        return Optional.ofNullable(replies.get(replyId));
    }

    @Override
    public List<Reply> findByArticleId(final Long articleId) {
        return replies.keySet()
                .stream()
                .filter(key -> replies.get(key).getArticleId().equals(articleId))
                .map(replies::get)
                .toList();
    }

    @Override
    public List<Reply> findAll() {
        return replies.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<Reply> update(final Reply reply) {
        return Optional.ofNullable(replies.replace(reply.getId(), reply));
    }

    @Override
    public void delete(final Long replyId) {
        replies.remove(replyId);
    }

    @Override
    public void deleteByArticleId(final Long articleId) {
        replies.entrySet()
                .removeIf(entry -> entry.getValue().getArticleId().equals(articleId));
    }
}
