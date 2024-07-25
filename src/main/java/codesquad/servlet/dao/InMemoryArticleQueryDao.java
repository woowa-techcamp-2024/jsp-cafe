package codesquad.servlet.dao;

import codesquad.domain.article.Article;
import codesquad.domain.article.ArticleDao;
import codesquad.servlet.dto.ArticleResponse;
import codesquad.domain.user.User;
import codesquad.domain.user.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryArticleQueryDao implements ArticleQueryDao {
    private final ArticleDao articleDao;
    private final UserDao userDao;

    public InMemoryArticleQueryDao(ArticleDao articleDao, UserDao userDao) {
        this.articleDao = articleDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<ArticleResponse> findById(Long id) {
        Optional<Article> findArticle = articleDao.findById(id);
        if (findArticle.isEmpty()) {
            return Optional.empty();
        }
        Article article = findArticle.get();
        Optional<User> findWriter = userDao.findByUserId(article.getWriter());
        if (findWriter.isEmpty()) {
            return Optional.of(new ArticleResponse(article.getId(), article.getTitle(), article.getContent(), 0L, "알수없는사용자"));
        }
        User user = findWriter.get();
        return Optional.of(new ArticleResponse(article.getId(), article.getTitle(), article.getContent(), user.getId(), user.getName()));
    }

    @Override
    public List<ArticleResponse> findAll() {
        List<Article> articles = articleDao.findAll();
        List<ArticleResponse> articleResponses = new ArrayList<>();
        for (Article article : articles) {
            Optional<User> findWriter = userDao.findByUserId(article.getWriter());
            ArticleResponse response;
            if (findWriter.isEmpty()) {
                response = new ArticleResponse(article.getId(), article.getTitle(), article.getContent(), 0L, "알수없는사용자");
            } else {
                User user = findWriter.get();
                response = new ArticleResponse(article.getId(), article.getTitle(), article.getContent(), user.getId(), user.getName());
            }
            articleResponses.add(response);
        }
        return articleResponses;
    }
}
