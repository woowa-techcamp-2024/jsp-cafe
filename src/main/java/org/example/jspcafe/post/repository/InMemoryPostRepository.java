package org.example.jspcafe.post.repository;

import org.example.jspcafe.InMemoryRepository;
import org.example.jspcafe.post.model.Post;

import java.util.List;

public class InMemoryPostRepository extends InMemoryRepository<Post> implements PostRepository {

    @Override
    public List<Post> findAll() {
        return storage.values().stream()
                .toList();
    }

    @Override
    public List<Post> findAll(int offset, int limit) {
        return storage.values().stream()
                .skip(offset)
                .limit(limit)
                .toList();
    }

    protected InMemoryPostRepository() {
        super(Post.class);
    }

}
