package org.example.post.model.dto;

import java.time.LocalDateTime;
import org.example.post.model.PostStatus;
import org.example.post.model.dao.Post;

public class PostDto {

    private Long id;
    private String writer;
    private String title;
    private String contents;
    private PostStatus status;
    private LocalDateTime createdAt;

    public static PostDto toResponse(Post post) {
        PostDto postDto = new PostDto();
        postDto.id = post.getId();
        postDto.writer = post.getWriter();
        postDto.title = post.getTitle();
        postDto.contents = post.getContents();
        postDto.status = post.getPostStatus();
        postDto.createdAt = post.getCreatedAt();
        return postDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
