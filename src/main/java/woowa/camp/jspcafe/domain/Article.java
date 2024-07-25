package woowa.camp.jspcafe.domain;

import java.time.LocalDate;

public class Article {

    private Long id;
    private final Long authorId;

    private final String title;
    private final String content;
    private Integer hits;

    private final LocalDate createdAt;

    private Article(Long authorId, String title, String content, Integer hits, LocalDate createdAt) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.createdAt = createdAt;
    }

    public static Article create(Long authorId, String title, String content, LocalDate createdAt) {
        if (isAnonymousAuthor(authorId)) {
            return new Article(null, title, content, 0, createdAt);
        }
        return new Article(authorId, title, content, 0, createdAt);
    }

    private static boolean isAnonymousAuthor(Long authorId) {
        return authorId == null;
    }

    public boolean isAnonymousAuthor() {
        return authorId == null;
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getHits() {
        return hits;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void upHits() {
        hits++;
    }

    /**
     * only allowed in repository
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", hits=" + hits +
                ", createdAt=" + createdAt +
                '}';
    }
}
