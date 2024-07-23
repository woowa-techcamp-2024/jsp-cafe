package org.example.jspcafe.post.repository;

import org.example.jspcafe.Repository;
import org.example.jspcafe.post.model.Post;

import java.util.List;

public interface PostRepository extends Repository<Post> {
    List<Post> findAll();
    List<Post> findAll(int offset, int limit);
    int count();
}
