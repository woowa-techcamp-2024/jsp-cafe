package repository;

import domain.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryArticleRepository implements ArticleRepository{

    private final Map<Long, Article> articleMap;

    public MemoryArticleRepository(Map<Long, Article> map) {
        this.articleMap = map;
    }

    public void saveArticle(Article article) {
        articleMap.put(article.getId(), article);
    }

    public List<Article> findAll() {
        return new ArrayList<>(articleMap.values());
    }

    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(articleMap.get(id));
    }

}
