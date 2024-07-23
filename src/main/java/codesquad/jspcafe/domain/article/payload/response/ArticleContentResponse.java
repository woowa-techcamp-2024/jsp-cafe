package codesquad.jspcafe.domain.article.payload.response;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.article.domain.Article;
import java.time.LocalDateTime;

public class ArticleContentResponse {

    private final String title;
    private final String writer;
    private final String createdAt;

    private ArticleContentResponse(String title, String writer, LocalDateTime createdAt) {
        this.title = title;
        this.writer = writer;
        this.createdAt = DateTimeFormatExecutor.execute(createdAt);
    }

    public static ArticleContentResponse from(Article article) {
        return new ArticleContentResponse(article.getTitle(), article.getWriter(),
            article.getCreatedAt());
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
