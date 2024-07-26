package woowa.cafe.domain;

public class Question {

    private String id;
    private String authorName;
    private String title;
    private String content;

    public Question(String authorName, String title, String content) {
        this.authorName = authorName;
        this.title = title;
        this.content = content;
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
}
