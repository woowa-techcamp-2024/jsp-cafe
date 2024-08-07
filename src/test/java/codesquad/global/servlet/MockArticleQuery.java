package codesquad.global.servlet;

import codesquad.article.handler.dto.request.ArticleQueryRequest;
import codesquad.article.handler.dto.response.ArticleDetailResponse;
import codesquad.article.handler.dto.response.ArticleResponse;
import codesquad.article.handler.dao.ArticleQuery;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MockArticleQuery implements ArticleQuery {
    private ArticleResponse articleResponse;

    @Override
    public Optional<ArticleResponse> findById(Long id) {
        if (articleResponse == null) {
            return Optional.empty();
        }
        return Optional.of(articleResponse);
    }

    @Override
    public Optional<ArticleDetailResponse> findDetailById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<ArticleResponse> findAll(ArticleQueryRequest queryRequest) {
        if (articleResponse == null) {
            return Collections.emptyList();
        }
        return List.of(articleResponse);
    }

    @Override
    public Long count(ArticleQueryRequest articleQueryRequest) {
        return 0L;
    }

    public void stub(ArticleResponse articleResponse) {
        this.articleResponse = articleResponse;
    }
}
