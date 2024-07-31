package org.example.service;

import java.util.List;
import org.example.dto.ArticleCreateReqDto;
import org.example.entity.Article;
import org.example.exception.NotSameAuthorException;
import org.example.repository.ArticleRepository;
import org.example.repository.ArticleRepositoryDBImpl;

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

    public void update(int i, String title, String content, String userId) throws NotSameAuthorException {
        // 게시글 찾고
        Article article = articleRepository.findById(i).orElseThrow(
            () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        // 게시글 작성자와 로그인한 사용자가 같은지 확인
        if (!article.isOwner(userId)) {
            throw new NotSameAuthorException("게시글 작성자만 수정할 수 있습니다.");
        }

        // 게시글 수정
        articleRepository.update(i, title, content, userId);
    }

    public void delete(int i, String userId) throws NotSameAuthorException {
        // 게시글 찾고
        Article article = articleRepository.findById(i).orElseThrow(
            () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        // 게시글 작성자와 로그인한 사용자가 같은지 확인
        if (!article.isOwner(userId)) {
            throw new NotSameAuthorException("게시글 작성자만 삭제할 수 있습니다.");
        }

        // 게시글 삭제
        articleRepository.deleteById(i);
    }
}
