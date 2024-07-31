package woopaca.jspcafe.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime writtenAt;
    private Long writerId;
    private ContentStatus status;

    public Post(Long id, String title, String content, Integer viewCount, LocalDateTime writtenAt, Long writerId, ContentStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.writtenAt = writtenAt;
        this.writerId = writerId;
        this.status = status;
    }

    public Post(String title, String content, Long writerId) {
        this(null, title, content, 0, LocalDateTime.now(), writerId, ContentStatus.PUBLISHED);
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

    public Long getWriterId() {
        return writerId;
    }

    public ContentStatus getStatus() {
        return status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void softDelete() {
        this.status = ContentStatus.DELETED;
    }

    public boolean isDeleted() {
        return status == ContentStatus.DELETED;
    }

    public boolean isPublished() {
        return status == ContentStatus.PUBLISHED;
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