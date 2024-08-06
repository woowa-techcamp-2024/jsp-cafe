package woopaca.jspcafe.repository;

import woopaca.jspcafe.model.Page;
import woopaca.jspcafe.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostMemoryRepository implements PostRepository {

    private static final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    @Override
    public void save(Post post) {
        if (post.getId() != null) {
            posts.put(post.getId(), post);
            return;
        }

        post.setId(sequence.incrementAndGet());
        posts.compute(post.getId(), (key, value) -> {
            if (value != null) {
                throw new IllegalArgumentException("[ERROR] duplicate key: " + key);
            }
            return post;
        });
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    @Override
    public Page<Post> findToPage(int page, int limit) {
        List<Post> posts = List.copyOf(PostMemoryRepository.posts.values());
        int totalPage = (int) Math.ceil((double) posts.size() / limit);
        int fromIndex = (page - 1) * limit;
        int toIndex = Math.min(fromIndex + limit, posts.size());
        return new Page<>(posts.subList(fromIndex, toIndex), totalPage, page, posts.size());
    }

    @Override
    public int countPublishedPosts() {
        return posts.size();
    }
}
