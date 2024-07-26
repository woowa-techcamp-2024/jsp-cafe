package domain;

public class Article {

    private Long id;
    private Users writer;
    private String title;
    private String content;
    private String created;

    public Article(Long id, Users writer, String title, String content, String created) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getWriter() {
        return writer;
    }

    public void setWriter(Users writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public String setTitle(String title) {
        return this.title = title;
    }

    public String getContent() {
        return content;
    }

    public String setContent(String content) {
        return this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public String setCreated(String created) {
        return this.created = created;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", writer=" + writer +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", created='" + created + '\'' +
                '}';
    }

}
