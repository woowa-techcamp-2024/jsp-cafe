package woowa.cafe.domain;

public class Question {

    private String id;
    private String authorName;
    private String title;
    private String content;
    private String userId;
    private String status;

    public Question(String authorName, String title, String content, String userId, String status) {
        this.authorName = authorName;
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

    // setter
    public void setTitle(String title) {
        if (title != null) this.title = title;
    }

    public void setContent(String content) {
        if (content != null) this.content = content;
    }
}
