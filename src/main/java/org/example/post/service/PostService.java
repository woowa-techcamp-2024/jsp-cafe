package org.example.post.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostResponse;
import org.example.post.repository.PostRepository;

@Component
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

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

    public PostResponse updatePost(PostResponse postDto) throws SQLException {
        Post post = Post.createWithId(postDto.getId(), postDto.getWriter(), postDto.getTitle(), postDto.getContents());
        return PostResponse.toResponse(postRepository.update(post));
    }

    public void deleteById(Long id) throws SQLException {
        // soft deleted이기 때문에 상태를 업데이트하는 쿼리 필요
        postRepository.delete(id);
    }
}
