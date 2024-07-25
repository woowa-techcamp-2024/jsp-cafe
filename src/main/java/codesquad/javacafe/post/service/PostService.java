package codesquad.javacafe.post.service;

import codesquad.javacafe.post.dto.request.PostCreateRequestDto;
import codesquad.javacafe.post.dto.response.PostResponseDto;
import codesquad.javacafe.post.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PostService {
    private static final PostService instance = new PostService();
    private PostService() {}
    public static PostService getInstance() {
        return instance;
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
