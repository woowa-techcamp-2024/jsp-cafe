package woopaca.jspcafe.service;

import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.repository.PostRepository;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.servlet.dto.response.PostDetailsResponse;
import woopaca.jspcafe.servlet.dto.response.PostsResponse;
import woopaca.jspcafe.servlet.dto.request.WritePostRequest;

import java.util.List;

public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void writePost(WritePostRequest writePostRequest, String userId) {
        /*User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 사용자를 찾을 수 없습니다."));*/
        Post post = new Post(writePostRequest.title(), writePostRequest.content(), "사용자");
        postRepository.save(post);
    }

    public List<PostsResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostsResponse::from)
                .toList();
    }

    public PostDetailsResponse getPostDetails(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 게시글을 찾을 수 없습니다. postId: " + postId));
        updateViewCount(post);
        List<Post> posts = postRepository.findAll();
        int postsSize = posts.size();
        int postIndex = posts.indexOf(post);
        boolean hasNext = postIndex < postsSize - 1;
        boolean hasPrevious = postIndex > 0;
        Long previousPostId = hasPrevious ? posts.get(postIndex - 1).getId() : null;
        Long nextPostId = hasNext ? posts.get(postIndex + 1).getId() : null;
        return PostDetailsResponse.of(post, hasNext, hasPrevious, nextPostId, previousPostId);
    }

    private void updateViewCount(Post post) {
        post.increaseViewCount();
        postRepository.save(post);
    }
}
