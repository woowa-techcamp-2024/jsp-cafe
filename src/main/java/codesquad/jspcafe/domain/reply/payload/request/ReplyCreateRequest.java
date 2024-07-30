package codesquad.jspcafe.domain.reply.payload.request;

import java.util.Map;

public class ReplyCreateRequest {

    private final Long article;
    private final String userId;
    private final String contents;

    private ReplyCreateRequest(Long article, String userId, String contents) {
        this.article = article;
        this.userId = userId;
        this.contents = contents;
    }

    public static ReplyCreateRequest of(Map<String, String[]> map, String userId) {
        return new ReplyCreateRequest(Long.parseLong(map.get("article")[0]), userId,
            map.get("contents")[0]);
    }

    public Long getArticle() {
        return article;
    }

    public String getUserId() {
        return userId;
    }

    public String getContents() {
        return contents;
    }

}
