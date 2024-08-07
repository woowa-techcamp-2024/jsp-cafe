package org.example.post.service;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.PostStatus;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PagedPostsResult;
import org.example.post.model.dto.PostDto;
import org.example.post.repository.PostRepository;
import org.example.reply.model.dto.ReplyDto;
import org.example.reply.repository.ReplyRepository;

@Component
public class PostService {

    private static final long CACHE_DURATION = 60000;
    private static final int DEFAULT_PAGE_SIZE = 15;

    private PostRepository postRepository;
    private ReplyRepository replyRepository;

    private volatile int cachedTotalPostCount = 0;
    private volatile long lastCacheTime = 0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

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

    public PagedPostsResult getPagedPosts(int page, int pageSize) throws SQLException {
        if (page < 1) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        int offset = (page - 1) * pageSize;
        List<PostDto> posts = postRepository.findAllWithPagination(offset, pageSize);

        int totalPosts = getTotalPostCount();
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        return new PagedPostsResult(posts, page, totalPages, totalPosts);
    }

    public int getTotalPostCount() {
        long currentTime = System.currentTimeMillis();
        lock.readLock().lock();
        try {
            if (currentTime - lastCacheTime <= CACHE_DURATION) {
                return cachedTotalPostCount;
            }
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            if (currentTime - lastCacheTime > CACHE_DURATION) {
                cachedTotalPostCount = postRepository.getTotalPostCount();
                lastCacheTime = currentTime;
            }
            return cachedTotalPostCount;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get total post count", e);
        } finally {
            lock.writeLock().unlock();
        }
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
