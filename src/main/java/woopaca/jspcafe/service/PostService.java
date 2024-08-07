package woopaca.jspcafe.service;

import woopaca.jspcafe.error.BadRequestException;
import woopaca.jspcafe.error.ForbiddenException;
import woopaca.jspcafe.error.NotFoundException;
import woopaca.jspcafe.model.Authentication;
import woopaca.jspcafe.model.Page;
import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.model.Reply;
import woopaca.jspcafe.model.User;
import woopaca.jspcafe.repository.PostRepository;
import woopaca.jspcafe.repository.ReplyRepository;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.servlet.dto.request.PostEditRequest;
import woopaca.jspcafe.servlet.dto.request.WritePostRequest;
import woopaca.jspcafe.servlet.dto.response.PostDetailsResponse;
import woopaca.jspcafe.servlet.dto.response.PostEditResponse;
import woopaca.jspcafe.servlet.dto.response.PostsPageResponse;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PostService {

    private static final int DEFAULT_PAGE_SIZE = 15;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
    }

    public void writePost(WritePostRequest writePostRequest, Authentication authentication) {
        User user = authentication.principal();
        validatePostTitleAndContent(writePostRequest.title(), writePostRequest.content());
        Post post = new Post(writePostRequest.title(), writePostRequest.content(), user.getId());
        postRepository.save(post);
    }

    public PostDetailsResponse getPostDetails(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("[ERROR] 게시글을 찾을 수 없습니다."));
        if (post.isDeleted()) {
            throw new BadRequestException("[ERROR] 삭제된 게시글입니다.");
        }

        updateViewCount(post);
        User user = userRepository.findById(post.getWriterId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 작성자를 찾을 수 없습니다."));
        return PostDetailsResponse.of(post, user);
    }

    private void updateViewCount(Post post) {
        post.increaseViewCount();
        postRepository.save(post);
    }

    public PostEditResponse getPostTitleContent(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("[ERROR] 게시글을 찾을 수 없습니다."));
        return new PostEditResponse(post.getId(), post.getTitle(), post.getContent());
    }

    public Post validateWriter(Long postId, Authentication authentication) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("[ERROR] 존재하지 않는 게시글입니다."));
        if (!authentication.isPrincipal(post.getWriterId())) {
            throw new ForbiddenException("[ERROR] 작성자만 수정•삭제할 수 있습니다.");
        }
        return post;
    }

    public void updatePost(Long postId, PostEditRequest postEditRequest, Authentication authentication) {
        Post post = validateWriter(postId, authentication);

        validatePostTitleAndContent(postEditRequest.title(), postEditRequest.content());
        post.update(postEditRequest.title(), postEditRequest.content());
        postRepository.save(post);
    }

    private void validatePostTitleAndContent(String title, String content) {
        if (Objects.isNull(title) || Objects.isNull(content) || title.isBlank() || content.isBlank()) {
            throw new BadRequestException("[ERROR] 제목과 내용은 필수 입력 사항입니다.");
        }

        if (title.length() < 2 || content.length() < 2 || title.length() > 30 || content.length() > 1000) {
            throw new BadRequestException("[ERROR] 제목: 2 ~ 30자, 내용: 2 ~ 1000자");
        }
    }

    public void deletePost(Long postId, Authentication authentication) {
        Post post = validateWriter(postId, authentication);
        List<Reply> writerReplies = validateOtherUserReplies(post);
        post.softDelete();
        writerReplies.forEach(Reply::softDelete);
        writerReplies.forEach(replyRepository::save);
        postRepository.save(post);
    }

    private List<Reply> validateOtherUserReplies(Post post) {
        List<Reply> replies = replyRepository.findByPostId(post.getId());
        replies.stream()
                .filter(Reply::isPublished)
                .filter(reply -> !reply.getWriterId().equals(post.getWriterId()))
                .findAny()
                .ifPresent(reply -> {
                    throw new BadRequestException("[ERROR] 다른 사용자의 댓글이 존재하여 삭제할 수 없습니다.");
                });
        return replies;
    }

    public PostsPageResponse getPostsPage(int page) {
        if (page < 1) {
            throw new BadRequestException("[ERROR] 페이지는 1 이상이어야 합니다.");
        }

        Page<Post> postsPage = postRepository.findToPage(page, DEFAULT_PAGE_SIZE);
        List<Post> posts = postsPage.data();
        // TODO join으로 변경하기
        List<String> writers = findWriters(posts);
        return PostsPageResponse.of(posts, writers, postsPage.currentPage(), postsPage.totalPage(), postsPage.total());
    }

    private List<String> findWriters(List<Post> posts) {
        return posts.stream()
                .map(Post::getWriterId)
                .map(userRepository::findById)
                .map(Optional::get)
                .map(User::getNickname)
                .toList();
    }
}
