package codesquad.servlet;

import codesquad.servlet.dao.ArticleQuery;
import codesquad.servlet.dto.ArticleResponse;

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
    public List<ArticleResponse> findAll() {
        if (articleResponse == null) {
            return Collections.emptyList();
        }
        return List.of(articleResponse);
    }

    public void stub(ArticleResponse articleResponse) {
        this.articleResponse = articleResponse;
    }
}
