package woopaca.jspcafe.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime writtenAt;
    private String writer;

    public Post(Long id, String title, String content, Integer viewCount, LocalDateTime writtenAt, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.writtenAt = writtenAt;
        this.writer = writer;
    }

    public Post(String title, String content, String writer) {
        this(null, title, content, 0, LocalDateTime.now(), writer);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getViewCount() {
        return viewCount;
    }

    public LocalDateTime getWrittenAt() {
        return writtenAt;
    }

    public String getWriter() {
        return writer;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}