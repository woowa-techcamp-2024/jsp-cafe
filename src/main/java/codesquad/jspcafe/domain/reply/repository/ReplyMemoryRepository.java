package codesquad.jspcafe.domain.reply.repository;

import codesquad.jspcafe.domain.reply.domain.Reply;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ReplyMemoryRepository implements ReplyRepository {

    private final Map<Long, Reply> map;
    private final AtomicLong key = new AtomicLong(1);

    public ReplyMemoryRepository() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public Reply save(Reply reply) {
        reply.setId(key.getAndAdd(1L));
        map.put(reply.getId(), reply);
        return reply;
    }

    @Override
    public Reply update(Reply reply) {
        map.put(reply.getId(), reply);
        return reply;
    }

    @Override
    public Optional<Reply> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Reply> findByArticleId(Long articleId) {
        return map.values().stream().filter(reply -> reply.getArticle().equals(articleId)).toList();
    }

    @Override
    public List<Reply> findByArticleId(Long articleId, Long replyId) {
        return map.values().stream().sorted((r1, r2) -> r2.getId().compareTo(r1.getId()))
            .filter(reply -> reply.getArticle().equals(articleId) && reply.getId().equals(replyId))
            .limit(6)
            .toList();
    }

    @Override
    public Long delete(Reply reply) {
        return map.remove(reply.getId()).getId();
    }
}
