package org.example.post.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.PostStatus;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostDto;
import org.example.post.repository.PostRepository;

@Component
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void create(Post post) throws SQLException {
        postRepository.save(post);
    }

    public List<PostDto> getAll() throws SQLException {
        return postRepository.findAll();
    }

    public PostDto getPostById(long id) throws SQLException {
        PostDto post = postRepository.findById(id);
        if (post.getStatus() == PostStatus.DELETED) {
            throw new IllegalArgumentException("Post with id " + id + " is deleted");
        }
        return post;
    }

    public void updatePost(String userId, PostDto postDto) throws SQLException {
        Post post = Post.createWithAll(postDto.getId(), userId, postDto.getTitle(), postDto.getContents(), postDto.getStatus(), postDto.getCreatedAt());
        postRepository.update(post);
    }

    public void deleteById(Long id) throws SQLException {
        // TODO: 댓글이 존재한다면 삭제 불가능 한 비즈니스 로직 추가 단 모든 댓글이 자신이 작성한것이라면 삭제 가능
        postRepository.delete(id);
    }
}
