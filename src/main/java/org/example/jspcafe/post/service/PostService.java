package org.example.jspcafe.post.service;

import org.example.jspcafe.Component;
import org.example.jspcafe.comment.model.Comment;
import org.example.jspcafe.comment.repository.CommentRepository;
import org.example.jspcafe.post.model.Post;
import org.example.jspcafe.post.repository.JdbcPostRepository;
import org.example.jspcafe.post.repository.PostRepository;
import org.example.jspcafe.post.request.PostCreateRequest;
import org.example.jspcafe.post.request.PostModifyRequest;
import org.example.jspcafe.post.response.PostListResponse;
import org.example.jspcafe.post.response.PostResponse;
import org.example.jspcafe.user.model.Nickname;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public void createPost(PostCreateRequest request) {
        final Long userId = request.userId();
        final String title = request.title();
        final String content = request.content();

        Post post = new Post(userId, title, content, LocalDateTime.now());

        postRepository.save(post);
    }

    public PostListResponse getPosts(int page, int size) {
        int offset = (page - 1) * size;
        final List<Post> posts = postRepository.findAll(offset, size);
        final Set<Long> collectUserId = posts.stream()
                .map(post -> post.getUserId())
                .collect(Collectors.toUnmodifiableSet());

        final Map<Long, Nickname> nicknameMap = userRepository.findAllById(collectUserId).stream()
                .collect(Collectors.toUnmodifiableMap(User::getUserId, User::getNickname));

        final List<PostResponse> postList = posts.stream()
                .map(post -> new PostResponse(
                        post.getPostId(),
                        post.getUserId(),
                        nicknameMap.get(post.getUserId()).getValue(),
                        post.getTitle().getValue(),
                        post.getContent().getValue(),
                        post.getCreatedAt())
                ).toList();

        final int totalElements = postRepository.count();

        return PostListResponse.of(totalElements, postList);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }


    public PostService(
            JdbcPostRepository postRepository,
            CommentRepository commentRepository,
            JdbcUserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public PostResponse getPost(String postId) {
        // TODO 나중에 조인 쿼리로 바꾸기
        return postRepository.findById(Long.parseLong(postId))
                .map(post -> {
                    final User user = userRepository.findById(post.getUserId())
                            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
                    return new PostResponse(
                            post.getPostId(),
                            post.getUserId(),
                            user.getNickname().getValue(),
                            post.getTitle().getValue(),
                            post.getContent().getValue(),
                            post.getCreatedAt()
                    );
                })
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    public void modifyPost(final PostModifyRequest request) {
        final Long userId = request.userId();
        final Long postId = request.postId();
        final String title = request.title();
        final String content = request.content();

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.canModifyBy(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        post.updateTitle(title);
        post.updateContent(content);

        postRepository.update(post);
    }

    /**
     * 게시글 삭제
     *
     *  {@link PostService#deleteIfAllCommentByPostUser(Post)}
     *
     * 게시글 작성자가 작성한 댓글만 존재할 경우
     * 댓글을 모두 삭제한 후 게시글 삭제
     *
     * 게시글 작성자가 작성한 댓글이 아닌 댓글이 존재할 경우
     * IllegalArgumentException 발생
     *
     * 댓글이 존재하지 않는 경우 게시글 삭제
     *
     * @param userId
     * @param postId
     */
    public void deletePost(Long userId, Long postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.canModifyBy(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        deleteIfAllCommentByPostUser(post);
        postRepository.delete(post);
    }

    /**
     * 모든 댓글이 게시글 작성자가 작성한 댓글인 경우
     * 댓글을 모두 삭제
     *
     * 만약 다른 사용자가 작성한 댓글이 하나라도 존재한다면
     * IllegalArgumentException 발생
     * @param post
     */
    private void deleteIfAllCommentByPostUser(final Post post) {
        final List<Comment> comments = commentRepository.findAllByPostId(post.getPostId());

        if (comments.isEmpty()) {
            return;
        }

        comments.stream()
                .filter(comment -> !comment.getUserId().equals(post.getUserId()))
                .findAny()
                .ifPresent(comment -> {
                    throw new IllegalArgumentException("댓글이 존재하는 게시글은 삭제할 수 없습니다.");
                });

        comments.forEach(commentRepository::delete);
    }
}
