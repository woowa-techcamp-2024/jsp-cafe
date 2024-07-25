package com.woowa.hyeonsik.service;

import com.woowa.hyeonsik.dao.ArticleDao;
import com.woowa.hyeonsik.domain.Article;

import java.util.List;

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
                .orElseThrow(IllegalArgumentException::new);
    }
}
