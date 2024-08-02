package com.codesquad.cafe.model.dto;

import com.codesquad.cafe.db.domain.Comment;
import com.codesquad.cafe.exception.ValidationException;
import com.codesquad.cafe.util.StringUtil;

public class CommentCreateRequest {

    private Long postId;

    private Long parentId;

    private String content;

    public CommentCreateRequest() {
    }

    public Long getPostId() {
        return postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getContent() {
        return content;
    }

    public void validate() {
        StringBuilder sb = new StringBuilder();
        boolean valid = true;
        if (postId == null || postId <= 0) {
            sb.append("postId 1이상의 자연수여야 합니다.").append("\n");
            valid = false;
        }
        if (parentId != null && parentId <= 0) {
            sb.append("parentId 1이상의 자연수여야 합니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(content)) {
            sb.append("content 필수입니다.").append("\n");
            valid = false;
        }
        if (!valid) {
            throw new ValidationException(sb.toString());
        }
    }

    public Comment toComment(Long userId) {
        return Comment.of(postId, parentId, userId, content);
    }

}
