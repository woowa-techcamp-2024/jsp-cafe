package codesquad.javacafe.post.service;

import codesquad.javacafe.post.dto.request.PostCreateRequestDto;
import codesquad.javacafe.post.dto.response.PostResponseDto;
import codesquad.javacafe.post.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private static final PostService instance = new PostService();
    private PostService() {}
    public static PostService getInstance() {
        return instance;
    }

    public PostResponseDto getPost(long id) {
        var post = PostRepository.getInstance().findById(id);
        if (Objects.isNull(post)) {
            // TODO Error 처리
            log.error("[PostService] post is null");
        }
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getAllPosts() {
        return PostRepository.getInstance().findAll()
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public void createPost(PostCreateRequestDto postDto) {
        PostRepository.getInstance().save(postDto);
    }
}
