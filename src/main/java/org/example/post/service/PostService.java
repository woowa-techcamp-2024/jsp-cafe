package org.example.post.service;

import java.sql.SQLException;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostResponse;
import org.example.post.repository.PostRepository;

public class PostService {

    private PostRepository postRepository = new PostRepository();

    public PostResponse create(Post post) throws SQLException {
        return PostResponse.toResponse(postRepository.save(post));
    }


}
