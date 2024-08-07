package codesquad.article.handler.dto.response;

import codesquad.comment.handler.dto.response.CommentResponse;

import java.util.List;

public record ArticleDetailResponse(
        ArticleResponse article,
        List<CommentResponse> comments
) {
}
