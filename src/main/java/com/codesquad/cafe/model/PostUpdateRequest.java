package com.codesquad.cafe.model;

import com.codesquad.cafe.exception.ValidationException;
import com.codesquad.cafe.util.StringUtils;

public class PostUpdateRequest {

    private Long authorId;

    private String title;

    private String content;

    private String fileName;

    public PostUpdateRequest(){}

    public PostUpdateRequest(Long authorId, String title, String content, String fileName) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
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

    public String getFileName() {
        return fileName;
    }

    public void validate() {
        StringBuilder sb = new StringBuilder();
        boolean valid = true;
        if (authorId == null || authorId <= 0) {
            sb.append("authorId는 1이상의 자연수여야 합니다.").append("\n");
            valid = false;
        }
        if (StringUtils.isBlank(title)) {
            sb.append("title은 필수입니다.").append("\n");
            valid = false;
        }
        if (StringUtils.isBlank(content)) {
            sb.append("content는 필수입니다.").append("\n");
            valid = false;
        }
        if (!valid) {
            throw new ValidationException(sb.toString());
        }
    }
}
