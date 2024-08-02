package woopaca.jspcafe.mock;

import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MockPostRepository implements PostRepository {

    private final Map<Long, Post> posts = new HashMap<>();
    private long id = 0;

    @Override
    public void save(Post post) {
        post.setId(++id);
        posts.put(post.getId(), post);
    }

    @Override
    public List<Post> findAll() {
        return List.copyOf(posts.values());
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }
}
