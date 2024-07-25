package org.example.service;

import java.util.List;
import org.example.dto.ArticleCreateReqDto;
import org.example.entity.Article;
import org.example.repository.ArticleRepository;
import org.example.repository.ArticleRepositoryMemoryImpl;

public class ArticleService {

    private final ArticleRepository articleRepository = ArticleRepositoryMemoryImpl.getInstance();

    public void save(ArticleCreateReqDto article) {
        // 게시글 저장
        articleRepository.save(article.toEntity());

    }

    public List<Article> findAll() {
        // 게시글 목록 조회
        return articleRepository.findAll();
    }
}
