package com.woowa.cafe.service;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.article.ArticleDto;
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

    public List<ArticleDto> findAll() {
        List<Article> articles = articleRepository.findAll();

        List<Member> members = memberRepository.findMembersByIds(articles.stream()
                .map(Article::getWriterId)
                .toList());

        return ArticleDto.mapToList(articles, members);
    }

    public ArticleDto findById(final Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Member member = memberRepository.findById(article.getWriterId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return ArticleDto.of(article, member);
    }
}
