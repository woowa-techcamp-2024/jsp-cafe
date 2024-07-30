package com.jspcafe.board.service;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.model.ArticleDao;
import com.jspcafe.exception.ArticleNotFoundException;

import java.util.List;

public class ArticleService {
    private final ArticleDao articleDao;

    public ArticleService(final ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public String write(final String title, final String nickname, final String content) {
        Article article = Article.create(title, nickname, content);
        articleDao.save(article);
        return article.id();
    }

    public Article findById(final String id) {
        return articleDao.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article id not found, id: " + id));
    }

    public List<Article> findAll() {
        return articleDao.findAll();
    }

    public void update(final String id, final String title, final String content) {
        Article article = findById(id);
        Article updateArticle = article.update(title, content);
        articleDao.update(updateArticle);
    }

    public void delete(final String id) {
        articleDao.delete(id);
    }
}
