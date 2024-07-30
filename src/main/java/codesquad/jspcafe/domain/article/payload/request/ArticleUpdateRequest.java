package codesquad.jspcafe.domain.article.payload.request;

import java.util.Map;

public class ArticleUpdateRequest {

    private final Long articleId;
    private final String title;
    private final String contents;
    private final Long userId;

    private ArticleUpdateRequest(Long articleId, String title, String contents, Long userId) {
        this.articleId = articleId;
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }

    public static ArticleUpdateRequest of(Map<String, String[]> map, Long userId) {
        Long articleId = Long.parseLong(map.get("id")[0]);
        String title = map.get("title")[0];
        String contents = map.get("contents")[0];
        return new ArticleUpdateRequest(articleId, title, contents, userId);
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}
