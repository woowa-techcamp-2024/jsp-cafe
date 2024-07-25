package codesquad.javacafe.post.dto.response;

import codesquad.javacafe.post.entity.Post;

import java.time.LocalDateTime;

public class PostResponseDto {
    private String writer;
    private String title;
    private String contents;
    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.writer = post.getWriter();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
