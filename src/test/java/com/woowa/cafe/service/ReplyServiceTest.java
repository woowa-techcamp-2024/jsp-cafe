package com.woowa.cafe.service;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.article.ArticleListDto;
import com.woowa.cafe.dto.article.ReplyDto;
import com.woowa.cafe.dto.article.SaveArticleDto;
import com.woowa.cafe.dto.article.SaveReplyDto;
import com.woowa.cafe.repository.member.InMemoryMemberRepository;
import com.woowa.cafe.repository.member.MemberRepository;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.InMemoryArticleRepository;
import com.woowa.cafe.repository.qna.InMemoryReplyRepository;
import com.woowa.cafe.repository.qna.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReplyServiceTest {

    ArticleService articleService;
    ReplyService replyService;
    ArticleRepository articleRepository;
    MemberRepository memberRepository;
    ReplyRepository replyRepository;
    private Member member = new Member("testId", "testPassword", "testName", "testEmail@test.com");

    @BeforeEach
    void setUp() {
        articleRepository = new InMemoryArticleRepository();
        memberRepository = new InMemoryMemberRepository();
        replyRepository = new InMemoryReplyRepository();

        articleService = new ArticleService(articleRepository, memberRepository, replyRepository);
        replyService = new ReplyService(articleRepository, replyRepository);
    }

    @Test
    @DisplayName("댓글 저장 테스트")
    void save() {
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        String replyContent = "replyContent";
        Long replyId = replyService.save(new SaveReplyDto(articleId, replyContent), writerId);
        List<ReplyDto> replies = articleService.findById(articleId).replies();
        List<ArticleListDto> articles = articleService.findAll();

        assertAll(
                () -> assertNotNull(replyId),
                () -> assertEquals(1, replies.size()),
                () -> assertEquals(replyContent, replies.get(0).contents()),
                () -> assertEquals(1, articles.get(0).replyCount())
        );
    }

    @Test
    @DisplayName("댓글 저장 실패 테스트 - 게시글이 없는 경우")
    void saveFail() {
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        String replyContent = "replyContent";
        assertThrows(IllegalArgumentException.class, () -> replyService.save(new SaveReplyDto(2L, replyContent), writerId));
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void delete() {
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        String replyContent = "replyContent";
        Long replyId = replyService.save(new SaveReplyDto(articleId, replyContent), writerId);
        replyService.delete(replyId, writerId);
        List<ReplyDto> replies = articleService.findById(articleId).replies();
        List<ArticleListDto> articles = articleService.findAll();


        assertAll(
                () -> assertEquals(0, replies.size()),
                () -> assertEquals(0, articles.get(0).replyCount())
        );
    }

    @Test
    @DisplayName("댓글 삭제 실패 테스트 - 댓글이 없는 경우")
    void deleteFail() {
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        String replyContent = "replyContent";
        Long replyId = replyService.save(new SaveReplyDto(articleId, replyContent), writerId);
        List<ReplyDto> replies = articleService.findById(articleId).replies();
        List<ArticleListDto> articles = articleService.findAll();

        assertThrows(IllegalArgumentException.class, () -> replyService.delete(2L, writerId));
    }

    @Test
    @DisplayName("댓글 삭제 실패 테스트 - 다른 사람이 삭제하는 경우")
    void deleteFail2() {
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        String replyContent = "replyContent";
        Long replyId = replyService.save(new SaveReplyDto(articleId, replyContent), writerId);
        List<ReplyDto> replies = articleService.findById(articleId).replies();
        List<ArticleListDto> articles = articleService.findAll();

        assertThrows(IllegalArgumentException.class, () -> replyService.delete(replyId, "otherId"));
    }

}
