package camp.woowa.jspcafe.model;

import java.time.LocalDateTime;

public class Question {
    private final Long id;
    private String title;
    private String content;
    private final String writer;
    private final Long writerId;
    private final LocalDateTime createdAt;

    public Question(Long id, String title, String content, String writer, Long writerId, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.writerId = writerId;
        this.createdAt = createdAt;
    }

    public Question(String title, String content, Long writerId) {
        this(null, title, content, null, writerId, null);
    }

    public Question(String title, String content, String writer, long writerId) {
        this(null, title, content, writer, writerId, null);
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

    public String getWriter() {
        return writer;
    }

    public Long getWriterId() {
        return writerId;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void update(String updatedTitle, String updatedContent) {
        if (updatedTitle != null && !updatedTitle.isEmpty()) {
            this.title = updatedTitle;
        }
        if (updatedContent != null && !updatedContent.isEmpty()) {
            this.content = updatedContent;
        }
    }
}
