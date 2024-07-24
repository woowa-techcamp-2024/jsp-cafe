package woopaca.jspcafe.service;

import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.repository.PostRepository;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.servlet.dto.WritePostRequest;

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
}
