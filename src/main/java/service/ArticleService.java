package service;

import domain.Article;
import dto.ArticleDao;
import exception.TomcatException;
import repository.article.ArticleRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public void saveArticle(ArticleDao articleDao) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Article article = new Article(null, articleDao.getWriter(), articleDao.getTitle(), articleDao.getContent(), now.format(formatter));
        articleRepository.saveArticle(article);
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new TomcatException("Article not found"));
    }

    public void updateArticle(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);
        articleRepository.updateArticle(article);
    }


    public void deleteArticle(Long id) {
        articleRepository.deleteArticle(id);
    }


}
