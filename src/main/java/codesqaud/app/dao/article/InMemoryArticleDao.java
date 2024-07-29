package codesqaud.app.dao.article;

import codesqaud.app.dto.ArticleDto;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static jakarta.servlet.http.HttpServletResponse.*;

public class InMemoryArticleDao implements ArticleDao {
    private static final Logger log = LoggerFactory.getLogger(InMemoryArticleDao.class);

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    private final Map<Long, Article> articles = new ConcurrentHashMap<>();

    @Override
    public void save(Article article) {
        if(article.getId() != null) {
            log.error("새로운 모델을 저장할 때 id를 명시적으로 지정하면 안됩니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        article.setId(ID_GENERATOR.incrementAndGet());
        articles.put(article.getId(), article);
    }

    @Override
    public void update(Article article) {
        if(article.getId() == null) {
            log.error("업데이트할 모델에 id를 지정하지 않았습니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        articles.compute(article.getId(), (id, existingArticle) -> {
            if(existingArticle == null) {
                log.info("업데이트 할 모델을 찾지 못했습니다.");
                throw new HttpException(SC_NOT_FOUND, "업데이트 할 qna 글을 찾지 못했습니다.");
            }
            return article;
        });
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
            throw new HttpException(SC_NOT_FOUND, "해당 qna 글은 존재하지 않습니다.");
        }
        articles.remove(article.getId());
    }

    @Override
    public List<ArticleDto> findAllAsDto() {
        throw new UnsupportedOperationException();
    }
}
