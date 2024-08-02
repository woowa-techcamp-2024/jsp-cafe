package repository.article;

import domain.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryArticleRepository implements ArticleRepository {

    private final Map<Long, Article> articleMap;
    private static final AtomicLong sequence = new AtomicLong();

    public MemoryArticleRepository(Map<Long, Article> map) {
        this.articleMap = map;
    }

    @Override
    public void saveArticle(Article article) {
        article.setId(sequence.incrementAndGet());
        articleMap.put(article.getId(), article);
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(articleMap.values());
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(articleMap.get(id));
    }

    @Override
    public void updateArticle(Article article) {
        articleMap.put(article.getId(), article);
    }

    @Override
    public void deleteArticle(Long id) {
        articleMap.remove(id);
    }

}
