package woopaca.jspcafe.mock;

import woopaca.jspcafe.model.Reply;
import woopaca.jspcafe.repository.ReplyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MockReplyRepository implements ReplyRepository {

    private final Map<Long, Reply> replies = new HashMap<>();
    private long id = 0;

    @Override
    public void save(Reply reply) {
        reply.setId(++id);
        replies.put(reply.getId(), reply);
    }

    @Override
    public List<Reply> findByPostId(Long postId) {
        return replies.values()
                .stream()
                .filter(reply -> reply.getPostId().equals(postId))
                .toList();
    }

    @Override
    public Optional<Reply> findById(Long id) {
        return Optional.ofNullable(replies.get(id));
    }
}
