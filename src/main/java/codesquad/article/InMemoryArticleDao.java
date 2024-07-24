package codesquad.article;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleDao implements ArticleDao {
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final ConcurrentMap<Long, Article> articles = new ConcurrentHashMap<>();

    @Override
    public Long save(Article article) {
        Long id;
        do {
            id = ID_GENERATOR.incrementAndGet();
            article = new Article(id, article);
        } while (articles.putIfAbsent(id, article) != null);
        return id;
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(articles.get(id));
    }

    @Override
    public List<Article> findAll() {
        return articles.values().stream().toList();
    }
}
