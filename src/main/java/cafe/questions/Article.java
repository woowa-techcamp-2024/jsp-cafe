package cafe.questions;

import java.sql.Date;
import java.sql.Timestamp;

public class Article {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String title;
    private final String content;
    private final Timestamp createdDate;
    private final Timestamp updatedDate;

    public Article(Long userId, String title, String content) {
        this(null, userId, null, title, content, null, null);
    }

    public Article(Long id, Long userId, String title, String content) {
        this(id, userId, null, title, content, null, null);
    }

    public Article(Long id, Long userId, String userName, String title, String content, Timestamp createdDate, Timestamp updatedDate) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Article withId(Long id) {
        return new Article(id, userId, userName, title, content, createdDate, updatedDate);
    }

    public Article withTitle(String title) {
        return new Article(id, userId, userName, title, content, createdDate, updatedDate);
    }

    public Article withContent(String content) {
        return new Article(id, userId, userName, title, content, createdDate, updatedDate);
    }

    public Article withCreatedDate(Timestamp createdDate) {
        return new Article(id, userId, userName, title, content, createdDate, updatedDate);
    }

    public Article withUpdatedDate(Timestamp updatedDate) {
        return new Article(id, userId, userName, title, content, createdDate, updatedDate);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedDate() {
        return new Date(createdDate.getTime());
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
