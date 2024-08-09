package com.codesquad.cafe.db.dao;

import java.util.List;
import java.util.Optional;

import com.codesquad.cafe.db.domain.Post;
import com.codesquad.cafe.exception.UnsupportedDBOperationException;
import com.codesquad.cafe.model.aggregate.PostWithAuthor;

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

	default void softDeletePostWithComments(Long postId) {
		throw new UnsupportedDBOperationException("updateDeleted is not supported");
	}

}
