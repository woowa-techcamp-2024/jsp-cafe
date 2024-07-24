package cafe.domain.db;

import cafe.domain.entity.Article;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ArticleDatabase {
    private final Map<String, Article> articleDatabase;

    public ArticleDatabase() {
        this.articleDatabase = new LinkedHashMap<>();
    }

    public void save(Article article) {
        articleDatabase.put(UUID.randomUUID().toString(), article);
    }

    public Article find(String id) {
        if (!articleDatabase.containsKey(id)) {
            return null;
        }
        return articleDatabase.get(id);
    }

    public Map<String, Article> findAll() {
        return articleDatabase;
    }
}
