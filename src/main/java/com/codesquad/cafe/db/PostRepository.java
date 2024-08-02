package com.codesquad.cafe.db;

import com.codesquad.cafe.db.domain.Post;
import com.codesquad.cafe.db.domain.PostDetail;
import com.codesquad.cafe.db.domain.PostWithAuthor;
import com.codesquad.cafe.exception.UnsupportedDBOperationException;
import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    default boolean existsById(Long id) {
        throw new UnsupportedDBOperationException("existsById is not supported");
    }

    int countAll();

    List<Post> findAll();

    Page<PostWithAuthor> findPostWithAuthorByPageSortByCreatedAtDesc(int pageNum, int pageSize);

    Optional<PostWithAuthor> findPostWithAuthorById(Long id);

    void deleteAll();

    default void deleteById(Long Id) {
        throw new UnsupportedDBOperationException("deleteById is not supported");
    }

    default void addView(Post post) {
        throw new UnsupportedDBOperationException("addView is not supported");
    }

    default Optional<PostDetail> findPostDetailById(Long id) {
        throw new UnsupportedDBOperationException("addView is not supported");
    }

    default void updateDeleted(Long id) {
        throw new UnsupportedDBOperationException("updateDeleted is not supported");
    }

}
