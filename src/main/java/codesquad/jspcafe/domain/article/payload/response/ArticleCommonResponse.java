package codesquad.jspcafe.domain.article.payload.response;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.user.domain.User;
import java.time.LocalDateTime;

public class ArticleCommonResponse {

    private final Long id;
    private final String title;
    private final String writerUserId;
    private final String writerUsername;
    private final String contents;
    private final String createdAt;

    private ArticleCommonResponse(Long id, String title, String writerUserId, String writerUsername,
        String contents, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.writerUserId = writerUserId;
        this.writerUsername = writerUsername;
        this.contents = contents;
        this.createdAt = DateTimeFormatExecutor.execute(createdAt);
    }

    public static ArticleCommonResponse from(Article article) {
        User writer = article.getWriter();
        return new ArticleCommonResponse(article.getId(),
            article.getTitle(),
            writer.getUserId(),
            writer.getUsername(),
            article.getContents(),
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

    public String getContents() {
        return contents;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
