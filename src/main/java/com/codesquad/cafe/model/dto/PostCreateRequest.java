package com.codesquad.cafe.model.dto;

import com.codesquad.cafe.db.domain.Post;
import com.codesquad.cafe.exception.ValidationException;
import com.codesquad.cafe.util.StringUtil;

public class PostCreateRequest {

    private Long authorId;

    private String title;

    private String content;

    public PostCreateRequest() {
    }

    public PostCreateRequest(Long authorId, String title, String content) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Post toPost() {
        return Post.of(authorId, title, content, null);
    }

    public void validate() {
        StringBuilder sb = new StringBuilder();
        boolean valid = true;
        if (authorId == null || authorId <= 0) {
            sb.append("authorId는 1이상의 자연수여야 합니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(title)) {
            sb.append("title은 필수입니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(content)) {
            sb.append("content는 필수입니다.").append("\n");
            valid = false;
        }
        if (!valid) {
            throw new ValidationException(sb.toString());
        }
    }

}
