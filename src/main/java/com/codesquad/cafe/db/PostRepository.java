package com.codesquad.cafe.db;

import com.codesquad.cafe.exception.UnsupportedDBOperationException;
import com.codesquad.cafe.db.entity.Post;
import com.codesquad.cafe.db.entity.PostDetailsDto;
import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    default boolean existsById(Long id){
        throw new UnsupportedDBOperationException("existsById is not supported");
    }

    int countAll();

    List<Post> findAll();

    Page<PostDetailsDto> findPostWithAuthorByPageSortByCreatedAtDesc(int pageNum, int pageSize);

    Optional<PostDetailsDto> findPostWithAuthorById(Long id);

    void deleteAll();

    default void deleteById(Long Id) {
        throw new UnsupportedDBOperationException("deleteById is not supported");
    }

    default void addView(Post post) {
        throw new UnsupportedDBOperationException("addView is not supported");
    }

}
