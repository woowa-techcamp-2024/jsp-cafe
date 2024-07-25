package repository;

import domain.Article;

import java.util.List;
import java.util.Optional;

public class JDBCArticleRepository implements ArticleRepository {

    @Override
    public void saveArticle(Article article) {

    }

    @Override
    public List<Article> findAll() {
        return List.of();
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.empty();
    }
}
