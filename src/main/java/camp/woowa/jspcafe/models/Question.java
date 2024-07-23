package camp.woowa.jspcafe.models;

public class Question {
    private final Long id;
    private final String title;
    private final String content;
    private final String writer;

    public Question(Long id, String title, String content, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
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
}
