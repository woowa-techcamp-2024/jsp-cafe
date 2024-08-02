package woowa.camp.jspcafe.service.dto.response;

import woowa.camp.jspcafe.domain.Article;

public class ArticleUpdateResponse {

    private Long id;
    private String title;
    private String content;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    private ArticleUpdateResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static ArticleUpdateResponse from(Article article) {
        return new ArticleUpdateResponse(article.getId(), article.getTitle(), article.getContent());
    }
}
