package cafe.questions;

import java.sql.Date;
import java.sql.Timestamp;

public class Article {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final Timestamp createdDate;
    private final Timestamp updatedDate;

    public Article(Long userId, String title, String content) {
        this(null, userId, title, content, null, null);
    }

    public Article(Long id, Long userId, String title, String content) {
        this(id, userId, title, content, null, null);
    }

    public Article(Long id, Long userId, String title, String content, Timestamp createdDate, Timestamp updatedDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Article withId(Long id) {
        return new Article(id, userId, title, content, createdDate, updatedDate);
    }

    public Article withTitle(String title) {
        return new Article(id, userId, title, content, createdDate, updatedDate);
    }

    public Article withContents(String content) {
        return new Article(id, userId, title, content, createdDate, updatedDate);
    }

    public Article withCreatedDate(Timestamp createdDate) {
        return new Article(id, userId, title, content, createdDate, updatedDate);
    }

    public Article withUpdatedDate(Timestamp updatedDate) {
        return new Article(id, userId, title, content, createdDate, updatedDate);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
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
}
