package codesquad.servlet.dao;

import codesquad.servlet.dto.ArticleResponse;

import java.util.List;
import java.util.Optional;

public interface ArticleQuery {
    Optional<ArticleResponse> findById(Long id);

    List<ArticleResponse> findAll();
}
