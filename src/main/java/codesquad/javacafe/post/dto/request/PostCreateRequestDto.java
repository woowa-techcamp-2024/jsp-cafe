package codesquad.javacafe.post.dto.request;

import codesquad.javacafe.post.entity.Post;

import java.util.Map;

public class PostCreateRequestDto {
    private String writer;
    private String title;
    private String contents;

    public PostCreateRequestDto(Map<String, String[]> body) {
        this.title = body.get("title")[0];
        this.contents = body.get("contents")[0];
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    @Override
    public String toString() {
        return "PostCreateRequestDto{" +
                "writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }

    public Post toEntity() {
        return new Post(this.writer,this.title,this.contents);
    }

}
