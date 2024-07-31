package woowa.cafe.domain;

public class Question {

    private String id;
    private String authorName;
    private String title;
    private String content;
    private String userId;

    public Question(String authorName, String title, String content, String userId) {
        this.authorName = authorName;
        this.title = title;
        this.content = content;
        this.userId = userId;
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

    // setter
    public void setTitle(String title) {
        if (title != null) this.title = title;
    }

    public void setContent(String content) {
        if (content != null) this.content = content;
    }
}
