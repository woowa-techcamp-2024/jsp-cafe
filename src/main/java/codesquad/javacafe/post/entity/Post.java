package codesquad.javacafe.post.entity;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Post {
    private static AtomicInteger ai = new AtomicInteger(0);
    private long id;
    private String writer;
    private String title;
    private String contents;
    private LocalDateTime createdAt;


    public Post(String writer, String title, String contents) {
        this.id = ai.incrementAndGet();
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();
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

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
