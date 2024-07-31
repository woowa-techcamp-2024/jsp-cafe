package com.woowa.cafe.service;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.article.ArticleDto;
import com.woowa.cafe.dto.article.SaveArticleDto;
import com.woowa.cafe.exception.HttpException;
import com.woowa.cafe.repository.member.MemberRepository;
import com.woowa.cafe.repository.qna.ArticleRepository;

import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public ArticleService(final ArticleRepository articleRepository, final MemberRepository memberRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
    }

    public Long save(final SaveArticleDto saveArticleDto, String writerId) {
        Member member = memberRepository.findById(writerId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 사용자입니다."));

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
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        Member member = memberRepository.findById(article.getWriterId())
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 사용자입니다."));

        return ArticleDto.of(article, member);
    }

    public void update(final Long articleId, final SaveArticleDto from, final String memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        if (!article.isSameWriter(memberId)) {
            throw new HttpException(SC_NOT_FOUND, "다른 사람이 수정할 수 없습니다.");
        }

        article.update(from.title(), from.content());
        articleRepository.update(article);
    }

    public void delete(final Long articleId, final String memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        if (!article.isSameWriter(memberId)) {
            throw new HttpException(SC_NOT_FOUND, "다른 사람이 삭제할 수 없습니다.");
        }

        articleRepository.delete(articleId);
    }
}
