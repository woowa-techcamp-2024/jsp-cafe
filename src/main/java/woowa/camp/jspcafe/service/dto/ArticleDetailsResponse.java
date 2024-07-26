package woowa.camp.jspcafe.service.dto;

import java.time.LocalDate;
import woowa.camp.jspcafe.domain.Article;

public class ArticleDetailsResponse {

    private Long articleId;
    private String title;
    private String content;
    private Integer hits;
    private LocalDate createdAt;
    private Long authorId;
    private String authorNickname;

    public ArticleDetailsResponse(Long articleId, String title, String content, Integer hits, LocalDate createdAt,
                                  Long authorId, String authorNickname) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.createdAt = createdAt;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
    }

    public static ArticleDetailsResponse of(Article article, Long authorId, String authorNickname) {
        return new ArticleDetailsResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
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

    public String getContent() {
        return content;
    }
}
