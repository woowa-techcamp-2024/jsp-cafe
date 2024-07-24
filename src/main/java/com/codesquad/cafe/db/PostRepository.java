package com.codesquad.cafe.db;

import com.codesquad.cafe.model.Post;
import com.codesquad.cafe.model.PostDetailsDto;
import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    Page<Post> findByPage(int pageNum, int pageSize);

    void deleteAll();

    List<PostDetailsDto> findAllPostWithDetail();
}
