package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class Article {

    private final Long id;
    private String writer;
    private String title;
    private String content;
    private String created;
    private static final AtomicLong sequence = new AtomicLong();

    public Article(String writer, String title, String content) {
        this.id = sequence.getAndIncrement();
        this.writer = writer;
        this.title = title;
        this.content = content;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.created = now.format(formatter);
    }

    public Long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }
}
