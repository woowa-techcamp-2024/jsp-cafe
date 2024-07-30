package dto;

import domain.User;

public class ArticleDao {

    private User writer;
    private String title;
    private String content;

    public ArticleDao(User writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public User getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
