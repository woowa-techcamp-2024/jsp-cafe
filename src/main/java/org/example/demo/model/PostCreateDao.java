package org.example.demo.model;

public class PostCreateDao {
    private Long writerId;
    private String title;
    private String contents;

    public PostCreateDao(Long writerId, String title, String contents) {
        this.writerId = writerId;
        this.title = title;
        this.contents = contents;
    }

    public Long getWriter() {
        return writerId;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}
