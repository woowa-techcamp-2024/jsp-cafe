package woopaca.jspcafe.repository;

import woopaca.jspcafe.model.Post;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostMemoryRepository implements PostRepository {

    private static final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    @Override
    public void save(Post post) {
        if (post.getId() != null) {
            posts.put(post.getId(), post);
        }

        post.setId(sequence.incrementAndGet());
        posts.compute(post.getId(), (key, value) -> {
            if (value != null) {
                throw new IllegalArgumentException("[ERROR] duplicate key: " + key);
            }
            return post;
        });
    }
}
