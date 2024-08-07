package com.woowa.database.reply;

import com.woowa.database.Page;
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

    @Override
    public Page<Reply> findAllByQuestionId(String questionId, int page, int size) {
        List<Reply> replyList = replies.values().stream()
                .filter(r -> r.getQuestionInfo().getQuestionId().equals(questionId))
                .sorted((a,b) -> {
                    if(a.getCreatedAt().isAfter(b.getCreatedAt())) {
                        return 1;
                    } else {
                        return -1;
                    }
                })
                .toList();
        List<Reply> content = replyList.subList(page * size, page * size + size);
        return Page.of(content, (long) replyList.size(), page, size);
    }
}
