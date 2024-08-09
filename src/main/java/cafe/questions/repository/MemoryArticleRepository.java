package cafe.questions.repository;

import cafe.questions.Article;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemoryArticleRepository implements ArticleRepository {
    private static final List<Article> articles = new CopyOnWriteArrayList<>();

    @Override
    public Article save(Article article) {
        if (article.getId() != null) {
            articles.set(Math.toIntExact(article.getId() - 1), article);
        } else {
            article = article.withId((long) (articles.size() + 1));

            articles.add(article);
        }
        return article;
    }

    @Override
    public List<Article> findAll() {
        return List.copyOf(articles);
    }

    @Override
    public Article findById(Long id) {
        return articles.get(Math.toIntExact(id - 1));
    }

    @Override
    public void deleteAll() {
        articles.clear();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Article> findAllPaginated(int page) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }
}
