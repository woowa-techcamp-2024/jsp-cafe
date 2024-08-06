package codesquad.article.service;

import codesquad.article.handler.dao.ArticleQuery;
import codesquad.article.handler.dto.request.ArticleQueryRequest;
import codesquad.article.handler.dto.response.ArticleResponse;
import codesquad.article.handler.dto.response.PagedArticleResponse;
import codesquad.common.http.response.PageInfo;
import codesquad.common.utility.PageUtils;

import java.util.List;
import java.util.Optional;

public class QueryArticleService {
    private final ArticleQuery articleQuery;

    public QueryArticleService(ArticleQuery articleQuery) {
        this.articleQuery = articleQuery;
    }

    public Optional<ArticleResponse> findById(Long id) {
        return articleQuery.findById(id);
    }

    public PagedArticleResponse<ArticleResponse> findAll(ArticleQueryRequest queryRequest) {
        List<ArticleResponse> content = articleQuery.findAll(queryRequest);
        PageInfo pageInfo = PageUtils.getPageInfo(articleQuery::count, queryRequest);
        return new PagedArticleResponse<>(content, pageInfo);
    }
}
