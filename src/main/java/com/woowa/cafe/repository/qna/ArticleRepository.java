package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.dto.article.ArticleQueryDto;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Long save(final Article article);

    Optional<Article> findById(final Long articleId);

    List<ArticleQueryDto> findByPage(final int page, final int size);

    List<Article> findAll();

    Optional<Article> update(final Article article);

    void delete(final Long articleId);

    int countByPage();
}
