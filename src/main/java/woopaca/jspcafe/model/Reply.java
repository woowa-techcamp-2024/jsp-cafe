package woopaca.jspcafe.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reply {

    private Long id;
    private String content;
    private LocalDateTime writtenAt;
    private Long writerId;
    private Long postId;
    private ContentStatus status;

    public Reply(Long id, String content, LocalDateTime writtenAt, Long writerId, Long postId, ContentStatus status) {
        this.id = id;
        this.content = content;
        this.writtenAt = writtenAt;
        this.writerId = writerId;
        this.postId = postId;
        this.status = status;
    }

    public Reply(String content, Long writerId, Long postId) {
        this(null, content, LocalDateTime.now(), writerId, postId, ContentStatus.PUBLISHED);
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getWrittenAt() {
        return writtenAt;
    }

    public Long getWriterId() {
        return writerId;
    }

    public Long getPostId() {
        return postId;
    }

    public ContentStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Reply reply = (Reply) o;
        return Objects.equals(id, reply.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
