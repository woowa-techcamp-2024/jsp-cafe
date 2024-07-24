package com.woowa.cafe.service;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.article.SaveArticleDto;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.member.MemberRepository;

import java.util.List;

public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public ArticleService(final ArticleRepository articleRepository, final MemberRepository memberRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
    }

    public Long save(final SaveArticleDto saveArticleDto, String writerId) {
        Member member = memberRepository.findById(writerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return articleRepository.save(saveArticleDto.toEntity(member.getMemberId()));
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(final Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }
}
