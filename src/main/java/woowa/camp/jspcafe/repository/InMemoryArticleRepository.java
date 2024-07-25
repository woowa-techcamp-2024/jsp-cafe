package woowa.camp.jspcafe.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import woowa.camp.jspcafe.domain.Article;

public class InMemoryArticleRepository implements ArticleRepository {

    private final Map<Long, Article> articles = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public Long save(Article article) {
        long currentId = this.id.incrementAndGet();
        article.setId(currentId);
        articles.put(currentId, article);
        return currentId;
    }

    @Override
    public Optional<Article> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(articles.get(id));
    }

    @Override
    public Optional<Article> findPrevious(Long currentId) {
        long previousId = currentId - 1;
        return Optional.ofNullable(articles.get(previousId));
    }

    @Override
    public Optional<Article> findNext(Long currentId) {
        long nextId = currentId + 1;
        return Optional.ofNullable(articles.get(nextId));
    }

}
