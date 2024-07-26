package service;

import domain.Article;
import domain.Users;
import dto.ArticleDao;
import repository.article.ArticleRepository;
import repository.users.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public void saveArticle(ArticleDao articleDao) {
        Users writer = userRepository.findByUserId(articleDao.getWriter())
                .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Article article = new Article(null, writer, articleDao.getTitle(), articleDao.getContent(), now.format(formatter));
        articleRepository.saveArticle(article);
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

}
