package org.example.post.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostResponse;
import org.example.post.repository.PostRepository;

public class PostService {

    private PostRepository postRepository = new PostRepository();

    public PostResponse create(Post post) throws SQLException {
        return PostResponse.toResponse(postRepository.save(post));
    }

    public List<PostResponse> getAll() throws SQLException {
        return postRepository.findAll().stream()
                .map(PostResponse::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public PostResponse getPostById(long id) throws SQLException {
        return PostResponse.toResponse(postRepository.findById(id));
    }


}
