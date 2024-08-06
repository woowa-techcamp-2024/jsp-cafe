package com.jspcafe.board.service;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.model.ArticleDao;
import com.jspcafe.exception.ArticleNotFoundException;
import java.util.List;

public class ArticleService {

  private static final int PAGE_SIZE = 15;
  private final ArticleDao articleDao;

  public ArticleService(final ArticleDao articleDao) {
    this.articleDao = articleDao;
  }

  public String write(final String userId, final String title, final String nickname,
      final String content) {
    Article article = Article.create(userId, title, nickname, content);
    articleDao.save(article);
    return article.id();
  }

  public Article findById(final String id) {
    return articleDao.findById(id)
        .orElseThrow(() -> new ArticleNotFoundException("Article id not found, id: " + id));
  }

  public int getTotalArticleCount() {
    return articleDao.getTotalArticleCount();
  }

  public int getTotalPages() {
    return (int) Math.ceil(getTotalArticleCount() * 1.0 / PAGE_SIZE);
  }

  public List<Article> findAll(final int page) {
    return articleDao.findAll(page, PAGE_SIZE);
  }

  public void update(final String id, final String title, final String content) {
    Article article = findById(id);
    Article updateArticle = article.update(title, content);
    articleDao.update(updateArticle);
  }

  public boolean delete(final String id, final String userId) {
    return articleDao.delete(id, userId);
  }
}
