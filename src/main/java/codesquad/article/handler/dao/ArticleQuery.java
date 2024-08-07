package codesquad.article.handler.dao;

import codesquad.article.handler.dto.request.ArticleQueryRequest;
import codesquad.article.handler.dto.response.ArticleDetailResponse;
import codesquad.article.handler.dto.response.ArticleResponse;

import java.util.List;
import java.util.Optional;

public interface ArticleQuery {
    Optional<ArticleResponse> findById(Long id);

    Optional<ArticleDetailResponse> findDetailById(Long id);

    List<ArticleResponse> findAll(ArticleQueryRequest articleQueryRequest);

    Long count(ArticleQueryRequest articleQueryRequest);
}
