package codesqaud.app.dto;

import codesqaud.app.model.Article;

public record ArticleDto(Long id, String title, String contents, String authorId) {

    public static ArticleDto fromPostAndUser(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContents(),
                article.getAuthorId()
        );
    }
}
