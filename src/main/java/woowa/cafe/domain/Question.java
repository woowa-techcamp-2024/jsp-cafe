package woowa.cafe.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Question {

    private String id;
    private String authorName;
    private String title;
    private String content;
    private String userId;
    private String status;
    private LocalDateTime createdAt;

    public Question(String authorName, String title, String content, String userId, String status, LocalDateTime createdAt) {
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // setter
    public void setTitle(String title) {
        if (title != null) this.title = title;
    }

    public void setContent(String content) {
        if (content != null) this.content = content;
    }
}
