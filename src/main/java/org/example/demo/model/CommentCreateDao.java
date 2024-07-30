package org.example.demo.model;

public class CommentCreateDao {
    private Long postId;
    private Long writerId;
    private String contents;

    public CommentCreateDao(Long postId, Long writerId, String contents) {
        this.postId = postId;
        this.writerId = writerId;
        this.contents = contents;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getWriterId() {
        return writerId;
    }

    public String getContents() {
        return contents;
    }
}
