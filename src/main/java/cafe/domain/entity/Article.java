package cafe.domain.entity;

import java.time.LocalDateTime;

public class Article {
    private final String writer;
    private final String title;
    private final String contents;
    private final String created;

    public static Article of(String writer, String title, String contents) {
        return new Article(writer, title, contents, LocalDateTime.now().toString());
    }

    private Article(String writer, String title, String contents, String created) {
        validate(writer, title, contents);
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.created = created;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getCreated() {
        return created;
    }

    public void validateWriter(String writer) {
        if (writer == null || writer.isEmpty()) {
            throw new IllegalArgumentException("Writer is empty!");
        }
    }

    public void validateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title is empty!");
        }
    }

    public void validateContents(String contents) {
        if (contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("Contents is empty!");
        }
    }

    public void validate(String writer, String title, String contents) {
        validateWriter(writer);
        validateTitle(title);
        validateContents(contents);
    }
}
