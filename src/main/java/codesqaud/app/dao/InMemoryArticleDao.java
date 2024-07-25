package codesqaud.app.dao;

import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class InMemoryArticleDao implements ArticleDao {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    private final Map<Long, Article> articles = new ConcurrentHashMap<>();

    @Override
    public void save(Article article) {
        if (article.getId() != null) {
            articles.put(article.getId(), article);
            return;
        }

        article.setId(ID_GENERATOR.incrementAndGet());
        articles.put(article.getId(), article);
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(articles.get(id));
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }

    @Override
    public void delete(Article article) {
        if (!articles.containsKey(article.getId())) {
            throw new HttpException(SC_INTERNAL_SERVER_ERROR, "해당 기사는 존재하지 않습니다.");
        }
        articles.remove(article.getId());
    }
}
