package org.example.post.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.PostStatus;
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
        Post post = postRepository.findById(id);
        if (post.getPostStatus() == PostStatus.DELETED) {
            throw new IllegalArgumentException("Post with id " + id + " is deleted");
        }
        return PostResponse.toResponse(post);
    }

    public PostResponse updatePost(PostResponse postDto) throws SQLException {
        Post post = Post.createWithId(postDto.getId(), postDto.getWriter(), postDto.getTitle(), postDto.getContents());
        return PostResponse.toResponse(postRepository.update(post));
    }

    public void deleteById(Long id) throws SQLException {
        // TODO: 댓글이 존재한다면 삭제 불가능 한 비즈니스 로직 추가
        postRepository.delete(id);
    }
}
