package codesquad.javacafe.post.service;

import codesquad.javacafe.common.exception.ClientErrorCode;
import codesquad.javacafe.common.exception.ServerErrorCode;
import codesquad.javacafe.post.dto.request.PostRequestDto;
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

    public static void updatePost(PostRequestDto postDto) {
        int update = PostRepository.getInstance().update(postDto);
        if (update == 0) {
            throw ClientErrorCode.POST_IS_NULL.customException("request post info = " + postDto);
        }
    }

    public PostResponseDto getPost(long id) {
        var post = PostRepository.getInstance().findById(id);
        if (Objects.isNull(post)) {
            log.error("[PostService] post is null");
            throw ServerErrorCode.INTERNAL_SERVER_ERROR.customException("post id = "+id);
        }
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getAllPosts() {
        var postList = PostRepository.getInstance().findAll();
        if (postList == null) {
            log.debug("[PostService] postList is null");
            return null;
        }
        return postList.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public void createPost(PostRequestDto postDto) {
        PostRepository.getInstance().save(postDto);
    }
}
