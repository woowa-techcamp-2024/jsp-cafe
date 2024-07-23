package codesquad.jspcafe.domain.article.repository;

import codesquad.jspcafe.domain.article.domain.Article;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ArticleRepository {

    private final Map<String, Article> map;

    public ArticleRepository() {
        map = new ConcurrentHashMap<>();
    }

    public Article save(Article article) {
        map.put(article.getTitle(), article);
        return article;
    }

    public Optional<Article> findByTitle(String title) {
        return Optional.ofNullable(map.get(title));
    }

    public List<Article> findAll() {
        return map.values().stream().toList();
    }
}
