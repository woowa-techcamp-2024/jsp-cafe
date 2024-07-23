package org.example.post.model.dao;

public class Post {

    private Long id;
    private String writer;
    private String title;
    private String contents;

    public static Post create(String writer, String title, String contents) {
        Post post = new Post();
        post.writer = writer;
        post.title = title;
        post.contents = contents;
        return post;
    }

    public static Post createWithId(Long id, String writer, String title, String contents) {
        Post post = new Post();
        post.id = id;
        post.writer = writer;
        post.title = title;
        post.contents = contents;
        return post;
    }

    public Long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "Post{" +
                "writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
