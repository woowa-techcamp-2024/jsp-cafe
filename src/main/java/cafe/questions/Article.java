package cafe.questions;

import java.util.Date;

public class Article {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String contents;
    private final String createdDate;
    private final String updatedDate;

    public Article(Long userId, String contents, String title) {
        this(null, userId, title, contents);
    }

    public Article(Long id, Long userId, String title, String contents) {
        this(id, userId, title, contents, new Date().toString(), new Date().toString());
    }

    private Article(Long id, Long userId, String title, String contents, String createdDate, String updatedDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Article withId(Long id) {
        return new Article(id, title, contents);
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

    public String getContents() {
        return contents;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}
