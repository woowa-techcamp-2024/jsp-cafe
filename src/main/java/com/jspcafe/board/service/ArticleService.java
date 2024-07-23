package com.jspcafe.board.service;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.model.ArticleDao;

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
}
