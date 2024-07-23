package org.example.post.model.dto;

import org.example.post.model.dao.Post;

public class PostResponse {

    private String writer;
    private String title;
    private String contents;

    public static PostResponse toResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setWriter(post.getWriter());
        postResponse.setTitle(post.getTitle());
        postResponse.setContents(post.getContents());
        return postResponse;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
