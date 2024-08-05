package codesquad.article.handler.dto.response;

public record ArticleResponse(
        Long articleId,
        String title,
        String content,
        Long writerId,
        String writer) {
}
