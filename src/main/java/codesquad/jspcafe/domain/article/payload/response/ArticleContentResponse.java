package codesquad.jspcafe.domain.article.payload.response;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.article.domain.Article;
import java.time.LocalDateTime;

public class ArticleContentResponse {

    private final Long id;
    private final String title;
    private final String writer;
    private final String createdAt;

    private ArticleContentResponse(Long id, String title, String writer, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createdAt = DateTimeFormatExecutor.execute(createdAt);
    }

    public static ArticleContentResponse from(Article article) {
        return new ArticleContentResponse(article.getId(), article.getTitle(), article.getWriter(),
            article.getCreatedAt());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
