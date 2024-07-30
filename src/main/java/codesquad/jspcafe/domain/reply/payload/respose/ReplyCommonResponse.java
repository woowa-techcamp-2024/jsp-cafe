package codesquad.jspcafe.domain.reply.payload.respose;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.reply.domain.Reply;
import codesquad.jspcafe.domain.user.domain.User;
import java.time.LocalDateTime;

public class ReplyCommonResponse {

    private final Long id;
    private final Long article;
    private final String userId;
    private final String username;
    private final String contents;
    private final String createdAt;

    private ReplyCommonResponse(Long id, Long article, String userId, String username,
        String contents, LocalDateTime createdAt) {
        this.id = id;
        this.article = article;
        this.userId = userId;
        this.username = username;
        this.contents = contents;
        this.createdAt = DateTimeFormatExecutor.execute(createdAt);
    }

    public static ReplyCommonResponse from(Reply reply) {
        User writer = reply.getWriter();
        return new ReplyCommonResponse(reply.getId(), reply.getArticle(), writer.getUserId(),
            writer.getUsername(),
            reply.getContents(), reply.getCreatedAt());
    }

    public Long getId() {
        return id;
    }

    public Long getArticle() {
        return article;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getContents() {
        return contents;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return """
            {
                "id": "%d",
                "article": "%d",
                "userId": "%s",
                "username": "%s",
                "contents": "%s",
                "createdAt": "%s"
            }
            """.formatted(id, article, userId, username, contents, createdAt);
    }
}
