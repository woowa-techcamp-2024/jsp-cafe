package com.woowa.database;

import com.woowa.model.Reply;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ReplyMemoryDatabase implements ReplyDatabase {

    private final Map<String, Reply> replies = new ConcurrentHashMap<>();

    @Override
    public void save(Reply reply) {
        replies.put(reply.getReplyId(), reply);
    }

    @Override
    public List<Reply> findAll() {
        return replies.values().stream().toList();
    }

    @Override
    public Optional<Reply> findById(String replyId) {
        return Optional.ofNullable(replies.get(replyId));
    }

    @Override
    public void delete(Reply reply) {

    }
}
