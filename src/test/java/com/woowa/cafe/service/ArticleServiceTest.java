package com.woowa.cafe.service;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.article.ArticleDto;
import com.woowa.cafe.dto.article.SaveArticleDto;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.InMemoryArticleRepository;
import com.woowa.cafe.repository.member.InMemoryMemberRepository;
import com.woowa.cafe.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ArticleServiceTest {

    ArticleService articleService;
    ArticleRepository articleRepository;
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        articleRepository = new InMemoryArticleRepository();
        memberRepository = new InMemoryMemberRepository();
        articleService = new ArticleService(articleRepository, memberRepository);
    }

    @Test
    @DisplayName("질문 저장 테스트")
    void save() {
        Member member = new Member("testId", "testPassword", "testName", "testEmail@test.com");
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        assertAll(() -> assertNotNull(articleId),
                () -> assertThat(articleRepository.findById(articleId).get().getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("질문 목록 조회 테스트")
    void findAll() {
        Member member = new Member("testId", "testPassword", "testName", "testEmail@test.com");
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);
        Long articleId1 = articleService.save(new SaveArticleDto(title + "1", content + "1"), writerId);

        List<ArticleDto> articles = articleService.findAll();

        assertAll(() -> assertThat(articles.size()).isEqualTo(2),
                () -> assertThat(articles.get(0).articleId()).isEqualTo(articleId),
                () -> assertThat(articles.get(1).articleId()).isEqualTo(articleId1),
                () -> assertThat(articles.get(0).title()).isEqualTo(title),
                () -> assertThat(articles.get(1).title()).isEqualTo(title + "1"),
                () -> assertThat(articles.get(0).contents()).isEqualTo(content),
                () -> assertThat(articles.get(1).contents()).isEqualTo(content + "1")
        );
    }

    @Test
    @DisplayName("질문 단건 조회 테스트")
    void findById() {
        Member member = new Member("testId", "testPassword", "testName", "testEmail@test.com");
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        ArticleDto article = articleService.findById(articleId);

        assertAll(() -> assertThat(article.articleId()).isEqualTo(articleId),
                () -> assertThat(article.title()).isEqualTo(title),
                () -> assertThat(article.contents()).isEqualTo(content),
                () -> assertThat(article.writerId()).isEqualTo(writerId),
                () -> assertThat(article.writerName()).isEqualTo(member.getName())
        );
    }

    @Test
    @DisplayName("질문 단건 조회 테스트 - 조회 실패")
    void findById_fail() {
        assertThatThrownBy(() -> articleService.findById(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 게시글입니다.");
    }
}
