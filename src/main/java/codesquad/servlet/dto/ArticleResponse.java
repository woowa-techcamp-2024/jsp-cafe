package codesquad.servlet.dto;

public record ArticleResponse(Long articleId,
                              String title,
                              String content,
                              Long writerId,
                              String writer
) {
}
