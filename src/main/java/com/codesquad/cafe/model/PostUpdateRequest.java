package com.codesquad.cafe.model;

public class PostUpdateRequest {

    private Long authorId;

    private String title;

    private String content;

    private String fileName;

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

}
