package com.woowa.hyeonsik.application.service;

import com.woowa.hyeonsik.application.dao.ArticleDao;
import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.exception.AuthorizationException;

import java.util.List;
import java.util.Optional;

public class ArticleService {
    private final ArticleDao articleDao;

    public ArticleService(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void write(Article article) {
        articleDao.save(article);
    }

    public List<Article> list() {
        return articleDao.findAll();
    }

    public Article findById(long articleId) {
        return articleDao.findByArticleId(articleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    public void update(Article article, String userId) {
        Optional<Article> foundArticle = articleDao.findByArticleId(article.getId());
        if (!foundArticle.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        if (!foundArticle.get().getWriter().equals(userId)) {
            throw new AuthorizationException("유저 정보가 일치하지 않습니다.");
        }

        articleDao.update(article);
    }

    public void remove(long articleId, String userId) {
        Optional<Article> foundArticle = articleDao.findByArticleId(articleId);
        if (!foundArticle.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        if (!foundArticle.get().getWriter().equals(userId)) {
            throw new AuthorizationException("유저 정보가 일치하지 않습니다.");
        }

        articleDao.removeByArticleId(articleId);
    }
}
