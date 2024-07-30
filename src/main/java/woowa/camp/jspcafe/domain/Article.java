package woowa.camp.jspcafe.domain;

import java.time.LocalDate;
import woowa.camp.jspcafe.domain.exception.ArticleException;

public class Article {

    private Long id;
    private Long authorId;

    private String title;
    private String content;
    private Integer hits;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    private Article(Long id, Long authorId, String title, String content, Integer hits, LocalDate createdAt,
                    LocalDate updatedAt
    ) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Article(Long authorId, String title, String content, Integer hits, LocalDate createdAt,
                   LocalDate updatedAt
    ) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Article create(Long authorId, String title, String content, LocalDate createdAt) {
        if (isAnonymousAuthor(authorId)) {
            throw new ArticleException("작성자 ID가 null 입니다. 익명 사용자는 게시글을 작성할 수 없습니다.");
        }
        return new Article(authorId, title, content, 0, createdAt, createdAt);
    }

    public static Article update(Article originArticle, String title, String content, LocalDate updatedAt) {
        return new Article(originArticle.getId(),
                originArticle.getAuthorId(),
                title,
                content,
                originArticle.getHits(),
                originArticle.getCreatedAt(),
                updatedAt);
    }

    private static boolean isAnonymousAuthor(Long authorId) {
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

    public LocalDate getUpdatedAt() {
        return updatedAt;
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
