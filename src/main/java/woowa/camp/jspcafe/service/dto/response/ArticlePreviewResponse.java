package woowa.camp.jspcafe.service.dto.response;

import java.time.LocalDate;
import woowa.camp.jspcafe.domain.Article;

public class ArticlePreviewResponse {

    private Long articleId;
    private String title;
    private Integer hits;
    private LocalDate createdAt;
    private Long authorId;
    private String authorNickname;

    private ArticlePreviewResponse(Long articleId, String title, Integer hits, LocalDate createdAt, Long authorId,
                                   String authorNickname) {
        this.articleId = articleId;
        this.title = title;
        this.hits = hits;
        this.createdAt = createdAt;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
    }

    public static ArticlePreviewResponse of(Article article, Long authorId, String authorNickname) {
        return new ArticlePreviewResponse(
                article.getId(),
                article.getTitle(),
                article.getHits(),
                article.getCreatedAt(),
                authorId,
                authorNickname);
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getHits() {
        return hits;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    @Override
    public String toString() {
        return "ArticlePreviewResponse{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", hits=" + hits +
                ", createdAt=" + createdAt +
                ", authorId=" + authorId +
                ", authorNickname='" + authorNickname + '\'' +
                '}';
    }

}
