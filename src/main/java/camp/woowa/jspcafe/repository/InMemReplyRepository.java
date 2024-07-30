package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.Reply;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemReplyRepository implements ReplyRepository {
    private static final AtomicLong sequence_id = new AtomicLong(1L);
    private static final Map<Long, Reply> replies = new ConcurrentHashMap<>();

    @Override
    public Long save(Reply reply) {
        Long id = sequence_id.getAndIncrement();
        replies.put(id, new Reply(id, reply.getContent(), reply.getQuestionId(), reply.getWriter(), reply.getWriterId()));
        return id;
    }

    @Override
    public Reply findById(Long id) {
        return replies.get(id);
    }

    @Override
    public void deleteAll() {
        replies.clear();
        sequence_id.set(1L);
    }

    @Override
    public List<Reply> findByQuestionId(Long questionId) {
        return List.of((Reply) replies.values().stream()
                .filter(reply -> reply.getQuestionId().equals(questionId)));
    }
}
