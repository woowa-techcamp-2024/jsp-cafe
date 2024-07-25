package woowa.camp.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.fixture.ArticleFixture;
import woowa.camp.jspcafe.fixture.UserFixture;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.article.InMemoryArticleRepository;
import woowa.camp.jspcafe.repository.InMemoryUserRepository;
import woowa.camp.jspcafe.repository.UserRepository;
import woowa.camp.jspcafe.service.dto.ArticleDetailsResponse;
import woowa.camp.jspcafe.service.dto.ArticlePreviewResponse;
import woowa.camp.jspcafe.service.dto.ArticleWriteRequest;
import woowa.camp.jspcafe.utils.FixedDateTimeProvider;
import woowa.camp.jspcafe.utils.time.DateTimeProvider;

class ArticleServiceTest {

    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private DateTimeProvider fixedDateTime;
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleRepository = new InMemoryArticleRepository();
        userRepository = new InMemoryUserRepository();
        fixedDateTime = new FixedDateTimeProvider(2024, 7, 25);
        articleService = new ArticleService(articleRepository, userRepository, fixedDateTime);
    }

    @Nested
    @DisplayName("Describe_게시글을 작성하는 기능은")
    class WriteArticleTest {

        @Test
        @DisplayName("[Success] 유효한 데이터로 게시글을 작성할 수 있다")
        void writeArticleWithValidData() {
            // given
            long authorId = 1L;
            String title = "제목";
            String content = "내용";
            ArticleWriteRequest request = new ArticleWriteRequest(authorId, title, content);

            // when
            Article result = articleService.writeArticle(request);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getAuthorId()).isEqualTo(authorId);
            assertThat(result.getTitle()).isEqualTo(title);
            assertThat(result.getContent()).isEqualTo(content);
            assertThat(result.getCreatedAt()).isEqualTo(LocalDate.of(2024, 7, 25));
        }

        @Test
        @DisplayName("[Success] 익명 사용자로 게시글을 작성할 수 있다")
        void writeAnonymousArticle() {
            // given
            String title = "익명 제목";
            String content = "익명 내용";
            ArticleWriteRequest request = new ArticleWriteRequest(null, title, content);

            // when
            Article result = articleService.writeArticle(request);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getTitle()).isEqualTo(title);
            assertThat(result.getContent()).isEqualTo(content);
            assertThat(result.isAnonymousAuthor()).isTrue();
        }

    }

    @Nested
    @DisplayName("Describe_상세 게시글을 조회하는 기능은")
    class FindArticleTest {

        @Test
        @DisplayName("[Success] 존재하는 게시글을 조회할 수 있다")
        void findExistingArticle() {
            // given
            User user1 = setupUser();
            Long authorId = user1.getId();
            String title = "제목";
            String content = "내용";
            Article article = articleService.writeArticle(new ArticleWriteRequest(authorId, title, content));
            // when
            ArticleDetailsResponse response = articleService.findArticleDetails(article.getId());
            // then
            assertArticleDetailsResponse(response, article, title, content, authorId, user1.getNickname());
        }

        private User setupUser() {
            User user1 = UserFixture.createUser1();
            userRepository.save(user1);
            return user1;
        }

        @Test
        @DisplayName("[Success] 익명 게시글을 조회할 수 있다")
        void findAnonymousArticle() {
            // given
            String title = "익명 제목";
            String content = "익명 내용";
            Article article = articleService.writeArticle(new ArticleWriteRequest(null, title, content));

            // when
            ArticleDetailsResponse response = articleService.findArticleDetails(article.getId());

            // then
            assertArticleDetailsResponse(response, article, title, content, null, "익명");
        }

        private void assertArticleDetailsResponse(ArticleDetailsResponse response,
                                                  Article article,
                                                  String title,
                                                  String content,
                                                  Long authorId,
                                                  String authorNickname
        ) {
            assertThat(response).isNotNull();
            assertThat(response.getArticleId()).isEqualTo(article.getId());
            assertThat(response.getTitle()).isEqualTo(title);
            assertThat(response.getContent()).isEqualTo(content);
            assertThat(response.getAuthorId()).isEqualTo(authorId);
            assertThat(response.getAuthorNickname()).isEqualTo(authorNickname);
            assertThat(response.getCreatedAt()).isEqualTo(fixedDateTime.getNow());
            assertThat(response.getHits()).isEqualTo(article.getHits());
        }

        @Test
        @DisplayName("[Success] 조회수가 1 증가한다")
        void test() {
            // given
            ArticleWriteRequest articleWriteRequest = ArticleFixture.createArticleWriteRequestWithAuthorId(
                    null, fixedDateTime.getNow());
            Article article = articleService.writeArticle(articleWriteRequest);
            assertThat(article.getHits()).isEqualTo(0);
            // when
            articleService.findArticleDetails(article.getId());
            // then
            assertThat(article.getHits()).isEqualTo(1);

        }

        @Test
        @DisplayName("[Exception] 존재하지 않는 게시글을 조회하면 예외가 발생한다")
        void findNonExistingArticle() {
            // when & then
            assertThatThrownBy(() -> articleService.findArticleDetails(999L))
                    .isInstanceOf(ArticleException.class)
                    .hasMessageContaining("Article not found");
        }

    }

    @Nested
    @DisplayName("Describe_게시글 목록을 조회하는 기능은")
    class FindArticlesTest {

        @Test
        @DisplayName("[Success] 페이지별로 게시글 목록을 조회할 수 있다")
        void test1() {
            // given
            int count = 15;
            setupUsers(count);
            ArticleFixture.createMultipleArticleWriteRequests(count, fixedDateTime.getNow())
                    .forEach(articleWriteRequest -> articleService.writeArticle(articleWriteRequest));
            // when
            List<ArticlePreviewResponse> page1 = articleService.findArticleList(1);
            List<ArticlePreviewResponse> page2 = articleService.findArticleList(2);
            // then
            assertThat(page1).hasSize(10);
            assertThat(page2).hasSize(5);
        }

        private void setupUsers(int count) {
            UserFixture.createMultipleUsers(count, fixedDateTime.getNow())
                    .forEach(user -> userRepository.save(user));
        }

        @Test
        @DisplayName("[Success] 익명 게시글과 일반 게시글이 혼합된 목록을 조회할 수 있다")
        void findMixedArticleList() {
            // given
            User user1 = UserFixture.createUser(1, fixedDateTime.getNow());
            userRepository.save(user1);
            Article article1 = articleService.writeArticle(new ArticleWriteRequest(1L, "일반 제목", "일반 내용"));
            Article article2 = articleService.writeArticle(new ArticleWriteRequest(null, "익명 제목", "익명 내용"));
            // when
            List<ArticlePreviewResponse> responses = articleService.findArticleList(1);
            // then
            assertThat(responses).hasSize(2);
            assertArticleResponse(responses.get(0), "익명", null, article2);
            assertArticleResponse(responses.get(1), user1.getNickname(), user1.getId(), article1);
        }

        private void assertArticleResponse(ArticlePreviewResponse response,
                                           String expectedAuthorNickname,
                                           Long expectedAuthorId,
                                           Article expectedArticle
        ) {
            assertThat(response.getAuthorNickname()).isEqualTo(expectedAuthorNickname);
            assertThat(response.getAuthorId()).isEqualTo(expectedAuthorId);
            assertThat(response.getArticleId()).isEqualTo(expectedArticle.getId());
            assertThat(response.getHits()).isEqualTo(expectedArticle.getHits());
            assertThat(response.getCreatedAt()).isEqualTo(expectedArticle.getCreatedAt());
            assertThat(response.getTitle()).isEqualTo(expectedArticle.getTitle());
        }
    }

}