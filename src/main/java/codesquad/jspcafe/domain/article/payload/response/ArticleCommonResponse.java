package codesquad.jspcafe.domain.article.payload.response;

import codesquad.jspcafe.domain.article.domain.Article;

public class ArticleCommonResponse {

    private final String title;
    private final String writer;
    private final String contents;

    private ArticleCommonResponse(String title, String writer, String contents) {
        this.title = title;
        this.writer = writer;
        this.contents = contents;
    }

    public static ArticleCommonResponse from(Article article) {
        return new ArticleCommonResponse(article.getTitle(), article.getWriter(),
            article.getContents());
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }
}
