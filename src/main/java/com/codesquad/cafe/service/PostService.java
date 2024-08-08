package com.codesquad.cafe.service;

import com.codesquad.cafe.db.dao.CommentDao;
import com.codesquad.cafe.db.dao.PostRepository;
import com.codesquad.cafe.exception.ResourceNotFoundException;
import com.codesquad.cafe.model.aggregate.CommentWithUser;
import com.codesquad.cafe.model.aggregate.PostDetail;
import com.codesquad.cafe.model.aggregate.PostWithAuthor;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostService {
    private static final int DEFAULT_COMMENT_SIZE = 5;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PostRepository postRepository;

    private final CommentDao commentDao;

    public PostService(PostRepository postRepository, CommentDao commentDao) {
        this.postRepository = postRepository;
        this.commentDao = commentDao;
    }

    public PostDetail getPostDetail(Long postId) {
        PostWithAuthor postWithAuthor = postRepository.findPostWithAuthorById(postId)
                .orElseThrow(ResourceNotFoundException::new);
        List<CommentWithUser> comments = commentDao.findCommentsByPostId(postId);

        return new PostDetail(postWithAuthor, comments);
    }

    public PostDetail getPostDetailWithPagedComments(Long postId, Long lastCommentId, int pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_COMMENT_SIZE;
        }

        PostWithAuthor postWithAuthor = postRepository.findPostWithAuthorById(postId)
                .orElseThrow(ResourceNotFoundException::new);
        List<CommentWithUser> comments = commentDao.findNoOffsetCommentsByPostId(postId, lastCommentId, pageSize);

        return new PostDetail(postWithAuthor, comments);
    }

    public PostDetail getPostDetailWithPagedComments(Long postId) {
        return getPostDetailWithPagedComments(postId, null, DEFAULT_COMMENT_SIZE);
    }
}
