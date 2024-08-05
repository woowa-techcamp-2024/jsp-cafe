package codesquad.article.handler.dto.response;

import java.util.List;

public record ArticleDetailResponse(ArticleResponse article,
                                    List<CommentResponse> comments
) {
}
