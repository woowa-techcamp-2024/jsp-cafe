package codesquad.article.handler.dto.response;

import codesquad.common.http.response.PageInfo;
import codesquad.common.http.response.PageResponse;

import java.util.List;

public class PagedArticleResponse<T extends ArticleResponse> extends PageResponse<T> {
    public PagedArticleResponse(List<? extends T> content, PageInfo pageInfo) {
        super(content, pageInfo);
    }
}
