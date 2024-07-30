package codesquad.jspcafe.domain.article.repository;

import codesquad.jspcafe.domain.article.domain.Article;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleMemoryRepository implements ArticleRepository {

    private final Map<Long, Article> map;
    private final AtomicLong key = new AtomicLong(1);

    public ArticleMemoryRepository() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public Article save(Article article) {
        article.setId(key.getAndAdd(1L));
        map.put(article.getId(), article);
        return article;
    }

    @Override
    public Article update(Article article) {
        map.put(article.getId(), article);
        return article;
    }

    @Override
    public Long delete(Article article) {
        map.remove(article.getId());
        return article.getId();
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Article> findAll() {
        return map.values().stream().toList();
    }
}
