package com.hyeonuk.jspcafe.reply.dao;

import com.hyeonuk.jspcafe.reply.domain.Reply;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryReplyDao implements ReplyDao {
    private final Map<Long, Reply> replies = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Reply save(Reply reply) {
        if (reply.getId() == null) { // 새로운 저장
            reply.setId(idGenerator.getAndIncrement());
            replies.put(reply.getId(), reply);
        } else { // 기존의 Reply 업데이트
            replies.put(reply.getId(), reply);
        }
        return reply;
    }

    @Override
    public void deleteById(Long id) {
        Reply reply = replies.get(id);
        if (reply != null) {
            reply.setDeletedAt(new Date()); // 실제로 삭제하지 않고, 삭제된 날짜만 설정
        }
    }

    @Override
    public void deleteAllByArticleId(Long articleId) {
        replies.values().stream()
                .filter(reply -> articleId.equals(reply.getArticle().getId()))
                .forEach(reply -> reply.setDeletedAt(new Date())); // 삭제된 날짜 설정
    }

    @Override
    public List<Reply> findAllByArticleId(Long articleId) {
        return replies.values().stream()
                .filter(reply -> reply.getArticle().getId().equals(articleId) && reply.getDeletedAt() == null)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return replies.values()
                .size();
    }

    @Override
    public Optional<Reply> findById(Long id) {
        Reply reply = replies.get(id);
        if (reply != null && reply.getDeletedAt() == null) {
            return Optional.of(reply);
        }
        return Optional.empty();
    }
}
