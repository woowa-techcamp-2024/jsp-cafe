package org.example.jspcafe.post.repository;

import org.example.jspcafe.Component;
import org.example.jspcafe.InMemoryRepository;
import org.example.jspcafe.post.model.Post;

import java.util.List;

@Component
public class InMemoryPostRepository extends InMemoryRepository<Post> implements PostRepository {

    @Override
    public List<Post> findAll() {
        return storage.values().stream()
                .toList();
    }

    @Override
    public List<Post> findAll(int offset, int limit) {
        validateOffsetAndLimit(offset, limit);
        return storage.values().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .skip(offset)
                .limit(limit)
                .toList();
    }

    public InMemoryPostRepository() {
        super(Post.class);
    }

    private void validateOffsetAndLimit(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException("offset과 limit은 0 이상이어야 합니다.");
        }
    }

}
