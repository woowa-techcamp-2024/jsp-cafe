package org.example.demo;

public class Post {
    private Long id;
    private String writer;
    private String title;
    private String contents;

    public Post(Long id, String writer, String title, String contents) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
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

    public void setId(Long id) {
        this.id = id;
    }
}
