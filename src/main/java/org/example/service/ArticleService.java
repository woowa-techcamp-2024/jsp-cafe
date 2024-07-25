package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.dto.ArticleCreateReqDto;
import org.example.entity.Article;
import org.example.repository.ArticleRepository;
import org.example.repository.ArticleRepositoryDBImpl;
import org.example.repository.ArticleRepositoryMemoryImpl;

public class ArticleService {

    private final ArticleRepository articleRepository = ArticleRepositoryDBImpl.getInstance();

    public void save(ArticleCreateReqDto article) {
        // 게시글 저장
        articleRepository.save(article.toEntity());

    }

    public List<Article> findAll() {
        // 게시글 목록 조회
        return articleRepository.findAll();
    }

    public Article findById(int i) {
        // 게시글 상세 조회
        return articleRepository.findById(i).orElseThrow(
            () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
    }
}
