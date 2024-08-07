package woopaca.jspcafe.mock;

import woopaca.jspcafe.model.Page;
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
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    @Override
    public Page<Post> findToPage(int page, int limit) {
        List<Post> posts = List.copyOf(this.posts.values());
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
