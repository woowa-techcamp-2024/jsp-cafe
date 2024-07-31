package com.woowa.cafe.service;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.domain.Reply;
import com.woowa.cafe.dto.article.ArticleDto;
import com.woowa.cafe.dto.article.ArticleListDto;
import com.woowa.cafe.dto.article.SaveArticleDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ArticleServiceTest {

    ArticleService articleService;
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
    }

    @Test
    @DisplayName("질문 저장 테스트")
    void save() {
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
        memberRepository.save(member);
        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);
        Long articleId1 = articleService.save(new SaveArticleDto(title + "1", content + "1"), writerId);

        List<ArticleListDto> articles = articleService.findAll();

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

    @Test
    @DisplayName("질문 수정 테스트")
    void update() {
        memberRepository.save(member);

        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        articleService.save(new SaveArticleDto(title, content), writerId);
        articleService.update(articleId, new SaveArticleDto("updated title", "updated content"), writerId);

        ArticleDto article = articleService.findById(articleId);

        assertAll(() -> assertThat(article.articleId()).isEqualTo(articleId),
                () -> assertThat(article.title()).isEqualTo("updated title"),
                () -> assertThat(article.contents()).isEqualTo("updated content"),
                () -> assertThat(article.writerId()).isEqualTo(writerId),
                () -> assertThat(article.writerName()).isEqualTo(member.getName())
        );
    }

    @Test
    @DisplayName("질문 수정 테스트 - 수정 실패(글이 존재하지 않음)")
    void update_fail() {
        assertThatThrownBy(() -> articleService.update(-1L, new SaveArticleDto("title", "content"), "testId"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 게시글입니다.");
    }

    @Test
    @DisplayName("질문 수정 테스트 - 수정 실패(작성자가 아님)")
    void update_fail_not_writer() {
        memberRepository.save(member);

        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        articleService.save(new SaveArticleDto(title, content), writerId);

        assertThatThrownBy(() -> articleService.update(articleId, new SaveArticleDto("updated title", "updated content"), "notWriterId"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 사람이 수정할 수 없습니다.");
    }

    @Test
    @DisplayName("질문 삭제 테스트")
    void delete() {
        memberRepository.save(member);

        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        articleService.save(new SaveArticleDto(title, content), writerId);

        articleService.delete(articleId, writerId);

        assertAll(
                () -> assertThat(articleRepository.findById(articleId)).isEmpty()
        );
    }

    @Test
    @DisplayName("질문 삭제 테스트 - 삭제 실패(다른 사람의 댓글이 존재)")
    void delete_fail_exists_reply() {
        memberRepository.save(member);

        String writerId = member.getMemberId();
        String title = "title";
        String content = "content";

        Long articleId = articleService.save(new SaveArticleDto(title, content), writerId);

        articleService.save(new SaveArticleDto(title, content), writerId);

        replyRepository.save(new Reply(articleId, "otherId", "reply"));

        assertThatThrownBy(() -> articleService.delete(articleId, writerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 사람이 작성한 댓글이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("질문 삭제 테스트 - 삭제 실패(글이 존재하지 않음)")
    void delete_fail_not_exists() {
        memberRepository.save(member);

        String writerId = member.getMemberId();

        assertThatThrownBy(() -> articleService.delete(-1L, writerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 게시글입니다.");
    }

    @Test
    @DisplayName("질문 삭제 테스트 - 삭제 실패(작성자가 아님)")
    void delete_fail_not_writer() {
        memberRepository.save(member);

        String writerId = member.getMemberId();

        Long articleId = articleService.save(new SaveArticleDto("title", "content"), writerId);

        assertThatThrownBy(() -> articleService.delete(articleId, "notWriterId"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 사람이 삭제할 수 없습니다.");
    }

}
