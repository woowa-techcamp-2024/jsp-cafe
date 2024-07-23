package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Long save(final Article article);

    Optional<Article> findById(final Long articleId);

    List<Article> findAll();

    Optional<Article> update(final Article article);

    void delete(final Long articleId);
}
