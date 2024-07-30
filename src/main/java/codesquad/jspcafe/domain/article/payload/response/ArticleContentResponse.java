package codesquad.jspcafe.domain.article.payload.response;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.user.domain.User;
import java.time.LocalDateTime;

public class ArticleContentResponse {

    private final Long id;
    private final String title;
    private final String writerUserId;
    private final String writerUsername;
    private final String createdAt;

    private ArticleContentResponse(Long id, String title, String writerUserId,
        String writerUsername, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.writerUserId = writerUserId;
        this.writerUsername = writerUsername;
        this.createdAt = DateTimeFormatExecutor.execute(createdAt);
    }

    public static ArticleContentResponse from(Article article) {
        User writer = article.getWriter();
        return new ArticleContentResponse(article.getId(),
            article.getTitle(),
            writer.getUserId(),
            writer.getUsername(),
            article.getCreatedAt());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWriterUserId() {
        return writerUserId;
    }

    public String getWriterUsername() {
        return writerUsername;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
