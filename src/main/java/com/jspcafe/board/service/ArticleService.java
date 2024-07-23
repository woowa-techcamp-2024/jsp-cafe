package com.jspcafe.board.service;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.model.ArticleDao;
import com.jspcafe.exception.ArticleNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return articleDao.findAll().stream()
                .sorted(Comparator.comparing(Article::createAt).reversed())
                .collect(Collectors.toList());
    }
}
