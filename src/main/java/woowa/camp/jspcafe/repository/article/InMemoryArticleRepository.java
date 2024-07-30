package woowa.camp.jspcafe.repository.article;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import woowa.camp.jspcafe.domain.Article;

public class InMemoryArticleRepository {

    private final Map<Long, Article> articles = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();

    public Long save(Article article) {
        long currentId = this.id.incrementAndGet();
        article.setId(currentId);
        articles.put(currentId, article);
        return currentId;
    }

    public Optional<Article> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(articles.get(id));
    }

    public Optional<Article> findPrevious(Long currentId) {
        long previousId = currentId - 1;
        return Optional.ofNullable(articles.get(previousId));
    }

    public Optional<Article> findNext(Long currentId) {
        long nextId = currentId + 1;
        return Optional.ofNullable(articles.get(nextId));
    }

    public List<Article> findByOffsetPagination(int offset, int limit) {
        List<Article> orderedCreatedAtDesc = articles.values().stream()
                .sorted(Comparator.comparing(Article::getCreatedAt).reversed()
                        .thenComparing(Comparator.comparing(Article::getId).reversed()))
                .toList();

        int fromIdx = Math.min(offset, orderedCreatedAtDesc.size());    // idx = 0부터 시작하므로
        int toIdx = Math.min(fromIdx + limit, orderedCreatedAtDesc.size());

        return orderedCreatedAtDesc.subList(fromIdx, toIdx);
    }
}
