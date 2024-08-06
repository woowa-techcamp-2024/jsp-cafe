package org.example.post.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.PostStatus;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostDto;
import org.example.post.repository.PostRepository;
import org.example.reply.model.dto.ReplyDto;
import org.example.reply.repository.ReplyRepository;

@Component
public class PostService {

    private PostRepository postRepository;
    private ReplyRepository replyRepository;

    @Autowired
    public PostService(PostRepository postRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
    }

    public void create(Post post) throws SQLException {
        postRepository.save(post);
    }

    public List<PostDto> getAll() throws SQLException {
        return postRepository.findAll();
    }

    public List<PostDto> getPagedPosts(LocalDateTime cursorTimestamp, Long cursorId, int pageSize) throws SQLException {
        if (cursorTimestamp == null) {
            cursorTimestamp = LocalDateTime.now();
        }
        if (cursorId == null) {
            cursorId =Long.MAX_VALUE;
        }
        return postRepository.findAllWithPagination(cursorTimestamp, cursorId, pageSize);
    }

    public PostDto getPostById(long id) throws SQLException {
        PostDto post = postRepository.findById(id);
        if (post.getStatus() == PostStatus.DELETED) {
            throw new IllegalArgumentException("Post with id " + id + " is deleted");
        }
        return post;
    }

    public void updatePost(String userId, PostDto postDto) throws SQLException {
        Post post = Post.createWithAll(postDto.getId(), userId, postDto.getTitle(), postDto.getContents(),
                postDto.getStatus(), postDto.getCreatedAt());
        postRepository.update(post);
    }

    public void deleteById(Long id) throws SQLException {
        PostDto post = postRepository.findById(id);
        validatePostDeletion(post);
        postRepository.softDeletePostAndReplies(id);
    }

    private void validatePostDeletion(PostDto post) throws SQLException {
        List<ReplyDto> replies = replyRepository.findAll(post.getId());
        boolean hasForeignReplies = replies.stream()
                .anyMatch(reply -> !reply.getWriter().equals(post.getUsername()));

        if (hasForeignReplies) {
            throw new IllegalStateException("다른 사용자의 댓글이 존재하여 게시글을 삭제할 수 업습니다.");
        }
    }
}
