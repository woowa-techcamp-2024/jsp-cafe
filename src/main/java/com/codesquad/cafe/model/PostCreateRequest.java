package com.codesquad.cafe.model;

import com.codesquad.cafe.db.entity.Post;

public class PostCreateRequest {

    private Long authorId;

    private String title;

    private String content;

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
        return  Post.of(authorId, title, content, null);
    }

}
