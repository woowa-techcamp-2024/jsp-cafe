package codesquad.javacafe.post.repository;

import codesquad.javacafe.post.dto.request.PostCreateRequestDto;
import codesquad.javacafe.post.entity.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostRepository {
    private static final Logger log = LoggerFactory.getLogger(PostRepository.class);
    private static final Map<String, Post> map = new ConcurrentHashMap<>();
    private static final PostRepository instance = new PostRepository();
    private PostRepository() {}

    public static PostRepository getInstance() {
        return instance;
    }


    public void save(PostCreateRequestDto postDto) {
        Post post = postDto.toEntity();
        map.put(post.getWriter(), post);
        log.debug("[PostRepository] created post: {}", post);
    }

    public List<Post> findAll() {
        return map.values().stream().toList();
    }
}
