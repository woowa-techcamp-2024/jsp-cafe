package org.example.jspcafe.post.service;

import org.example.jspcafe.Component;
import org.example.jspcafe.post.model.Post;
import org.example.jspcafe.post.repository.InMemoryPostRepository;
import org.example.jspcafe.post.repository.PostRepository;
import org.example.jspcafe.post.request.PostCreateRequest;

import java.time.LocalDateTime;

@Component
public class PostService {

    private final PostRepository postRepository;

    public void createPost(PostCreateRequest request) {
        final Long userId = request.userId();
        final String title = request.title();
        final String content = request.content();

        Post post = new Post(userId, title, content, LocalDateTime.now());

        postRepository.save(post);
    }


    public PostService(InMemoryPostRepository postRepository) {
        this.postRepository = postRepository;
    }
}
