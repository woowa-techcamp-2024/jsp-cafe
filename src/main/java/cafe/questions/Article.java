package cafe.questions;

import java.util.Date;

public class Article {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final String createdDate;
    private final String updatedDate;

    public Article(Long userId, String content, String title) {
        this(null, userId, title, content);
    }

    public Article(Long id, Long userId, String title, String content) {
        this(id, userId, title, content, new Date().toString(), new Date().toString());
    }

    private Article(Long id, Long userId, String title, String content, String createdDate, String updatedDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Article withId(Long id) {
        return new Article(id, userId, title, content, createdDate, new Date().toString());
    }

    public Article withContents(String contents) {
        return new Article(id, userId, title, contents, createdDate, new Date().toString());
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

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", contents='" + content + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                '}';
    }
}
