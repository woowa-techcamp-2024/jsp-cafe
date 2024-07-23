package com.woowa.cafe.service;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.SaveArticleDto;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.InMemoryArticleRepository;
import com.woowa.cafe.repository.user.InMemoryMemberRepository;
import com.woowa.cafe.repository.user.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        memberRepository.save(new Member("testId", "testPassword", "testName", "testEmail@test.com"));
        String writerId = "testId";
        String title = "title";
        String content = "content";

        Long questionId = articleService.save(new SaveArticleDto(title, content), writerId);

        assertNotNull(questionId);
    }

}
