package woowa.camp.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.domain.exception.UnAuthorizationException;
import woowa.camp.jspcafe.fixture.ArticleFixture;
import woowa.camp.jspcafe.fixture.UserFixture;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.repository.ArticleDBSetupExtension;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.article.DBArticleRepository;
import woowa.camp.jspcafe.repository.dto.request.ArticleUpdateRequest;
import woowa.camp.jspcafe.repository.reply.DBReplyRepository;
import woowa.camp.jspcafe.repository.reply.ReplyDBSetupExtension;
import woowa.camp.jspcafe.repository.reply.ReplyRepository;
import woowa.camp.jspcafe.repository.user.InMemoryUserRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.response.ArticleDetailsResponse;
import woowa.camp.jspcafe.service.dto.response.ArticlePreviewResponse;
import woowa.camp.jspcafe.service.dto.response.ArticleUpdateResponse;
import woowa.camp.jspcafe.service.dto.request.ArticleWriteRequest;
import woowa.camp.jspcafe.utils.FixedDateTimeProvider;

class ArticleServiceTest {

    private DatabaseConnector databaseConnector;
    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private ReplyRepository replyRepository;
    private DateTimeProvider fixedDateTime;
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        databaseConnector = new DatabaseConnector();
        articleRepository = new DBArticleRepository(databaseConnector);
        userRepository = new InMemoryUserRepository();
        replyRepository = new DBReplyRepository(databaseConnector);
        fixedDateTime = new FixedDateTimeProvider(2024, 7, 25);
        articleService = new ArticleService(articleRepository, userRepository, replyRepository, fixedDateTime);
    }

    @Nested
    @DisplayName("Describe_게시글을 작성하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
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
        @DisplayName("[Success] 익명 사용자로 게시글을 작성할 수 없다")
        void writeAnonymousArticle() {
            // given
            String title = "익명 제목";
            String content = "익명 내용";
            ArticleWriteRequest request = new ArticleWriteRequest(null, title, content);

            // when then
            assertThatThrownBy(() -> articleService.writeArticle(request)).isInstanceOf(ArticleException.class);
        }

    }

    @Nested
    @DisplayName("Describe_상세 게시글을 조회하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
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

        @Test
        @DisplayName("[Success] 회원이 작성한 게시글을 조회할 수 있다")
        void findAnonymousArticle() {
            // given
            User user = setupUser();
            String title = "익명 제목";
            String content = "익명 내용";
            Article article = articleService.writeArticle(new ArticleWriteRequest(user.getId(), title, content));
            System.out.println("article = " + article);

            // when
            ArticleDetailsResponse response = articleService.findArticleDetails(article.getId());
            System.out.println("response = " + response);

            // then
            assertArticleDetailsResponse(response, article, title, content, user.getId(), user.getNickname());
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
            assertThat(response.getHits()).isEqualTo(article.getHits() + 1);
        }

        @Test
        @DisplayName("[Success] 조회수가 1 증가한다")
        void test() {
            // given
            User user = setupUser();
            ArticleWriteRequest articleWriteRequest = ArticleFixture.createArticleWriteRequestWithAuthorId(
                    user.getId(), fixedDateTime.getNow());
            Article article = articleService.writeArticle(articleWriteRequest);
            assertThat(article.getHits()).isEqualTo(0);
            // when
            articleService.findArticleDetails(article.getId());
            // then
            Article findArticle = articleRepository.findById(article.getId()).get();
            assertThat(findArticle.getHits()).isEqualTo(1);

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
    @ExtendWith(ArticleDBSetupExtension.class)
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
        @DisplayName("[Success] 여러 회원의 일반 게시글 목록을 조회할 수 있다")
        void findMixedArticleList() {
            // given
            User user1 = UserFixture.createUser(1, fixedDateTime.getNow());
            User user2 = UserFixture.createUser(2, fixedDateTime.getNow());
            userRepository.save(user1);
            userRepository.save(user2);
            Article article1 = articleService.writeArticle(new ArticleWriteRequest(1L, "일반 제목", "일반 내용"));
            Article article2 = articleService.writeArticle(new ArticleWriteRequest(2L, "익명 제목", "익명 내용"));
            // when
            List<ArticlePreviewResponse> responses = articleService.findArticleList(1);
            // then
            assertThat(responses).hasSize(2);
            assertArticleResponse(responses.get(0), user2.getNickname(), user2.getId(), article2);
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

    @Nested
    @DisplayName("수정할 게시글을 조회하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
    class FindUpdateArticleTest {

        @Test
        @DisplayName("[Success] 게시글을 수정하는 회원과 게시글 작성자의 이메일이 일치하면 조회에 성공한다")
        void successWhenEmailMatches() {
            // given
            User user = setupUser();
            Article article = setupArticle();
            // when
            ArticleUpdateResponse response = articleService.findUpdateArticle(user, article.getId());
            // then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(article.getId());
            assertThat(response.getTitle()).isEqualTo(article.getTitle());
            assertThat(response.getContent()).isEqualTo(article.getContent());
        }

        @Test
        @DisplayName("[Exception] 게시글을 수정하는 회원과 게시글 작성자의 이메일이 다르면 예외가 발생한다")
        void test2() {
            // given
            User user1 = new User("email@naver.com", "닉네임1", "123", fixedDateTime.getNow());
            userRepository.save(user1);

            User updateRequestUser = new User("email1@naver.com", "닉네임2", "12345", fixedDateTime.getNow());
            userRepository.save(user1);

            Article article = new Article(user1.getId(), "제목", "내용", 0, fixedDateTime.getNow(), fixedDateTime.getNow());
            articleRepository.save(article);
            // when then
            assertThatThrownBy(() -> articleService.findUpdateArticle(updateRequestUser, article.getId()))
                    .isInstanceOf(UnAuthorizationException.class);
        }

    }

    @Nested
    @DisplayName("게시글을 수정하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
    class UpdateArticleTest {

        @Test
        @DisplayName("[Success] 게시글을 수정하는 회원과 게시글 작성자의 이메일이 일치하면 수정에 성공한다")
        void test() {
            // given
            User user = setupUser();
            Article article = setupArticle();
            ArticleUpdateRequest updateRequest = new ArticleUpdateRequest("Updated Title", "Updated Content");
            // when
            articleService.updateArticle(user, article.getId(), updateRequest);

            // then
            Article updatedArticle = articleRepository.findById(article.getId()).orElseThrow();
            assertThat(updatedArticle.getTitle()).isEqualTo(updateRequest.title());
            assertThat(updatedArticle.getContent()).isEqualTo(updateRequest.content());
            assertThat(updatedArticle.getUpdatedAt()).isEqualTo(fixedDateTime.getNow());
            assertThat(updatedArticle.getHits()).isEqualTo(0);  // 수정 시 조회수는 변동 없음
        }

        @Test
        @DisplayName("[Exception] 게시글을 수정하는 회원과 게시글 작성자의 이메일이 다르면 예외가 발생한다")
        void test2() {
            // given
            User user1 = new User("email@naver.com", "닉네임1", "123", fixedDateTime.getNow());
            userRepository.save(user1);

            User updateRequestUser = new User("email1@naver.com", "닉네임2", "12345", fixedDateTime.getNow());
            userRepository.save(user1);

            Article article = new Article(user1.getId(), "제목", "내용", 0, fixedDateTime.getNow(), fixedDateTime.getNow());
            articleRepository.save(article);

            ArticleUpdateRequest updateRequest = new ArticleUpdateRequest("Updated Title", "Updated Content");
            // when then
            assertThatThrownBy(() -> articleService.updateArticle(updateRequestUser, article.getId(), updateRequest))
                    .isInstanceOf(UnAuthorizationException.class);
        }

    }

    @Nested
    @DisplayName("Describe_게시글을 삭제하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
    @ExtendWith(ReplyDBSetupExtension.class)
    class DeleteArticleTest {

        User user1;
        User user2;

        Article article1;

        @BeforeEach
        void setUp() {
            user1 = UserFixture.createUser(1, fixedDateTime.getNow());
            user2 = UserFixture.createUser(2, fixedDateTime.getNow());
            user1.setId(userRepository.save(user1));
            user2.setId(userRepository.save(user2));

            article1 = ArticleFixture.createArticleWithAuthorId(user1.getId(), fixedDateTime.getNow());
            article1.setId(articleRepository.save(article1));
        }

        @Test
        @DisplayName("[Success] 댓글이 없는 경우 게시글 삭제가 가능하다")
        void deleteArticleWithNoReplies() {
            // given
            Long articleId = article1.getId();

            // when
            articleService.deleteArticle(user1, articleId);

            // then
            assertThat(articleRepository.findById(articleId)).isNotPresent();
        }

        @Test
        @DisplayName("[Success] 게시글 작성자의 댓글만 있는 경우 게시글 삭제가 가능하고 댓글도 모두 삭제된다")
        void deleteArticleWithOnlyAuthorReplies() {
            // given
            Long articleId = article1.getId();
            LocalDateTime fixedTime = fixedDateTime.getNowAsLocalDateTime();
            Reply authorReply = new Reply(user1.getId(), articleId, "content", fixedTime, fixedTime, null);
            replyRepository.save(authorReply);

            // when
            articleService.deleteArticle(user1, articleId);

            // then
            assertThat(articleRepository.findById(articleId)).isNotPresent();
            assertThat(replyRepository.findByArticleId(articleId)).isEmpty();
        }

        @Test
        @DisplayName("[Exception] 게시글 작성자가 아니면 UnAuthorizationException 예외가 발생한다")
        void deleteArticleByNonAuthorThrowsException() {
            // given
            Long articleId = article1.getId();

            // when & then
            assertThatThrownBy(() -> articleService.deleteArticle(user2, articleId))
                    .isInstanceOf(UnAuthorizationException.class)
                    .hasMessageContaining("게시글 수정은 작성자의 이메일과 동일해야 합니다");
        }

        @Test
        @DisplayName("[Exception] 게시글 작성자가 아닌 회원이 작성한 댓글이 있으면 ArticleException 예외가 발생한다")
        void deleteArticleWithOtherUserReplyThrowsException() {
            // given
            Long articleId = article1.getId();
            LocalDateTime fixedTime = fixedDateTime.getNowAsLocalDateTime();
            Reply otherUserReply = new Reply(user2.getId(), articleId, "content", fixedTime, fixedTime, null);
            replyRepository.save(otherUserReply);

            // when & then
            assertThatThrownBy(() -> articleService.deleteArticle(user1, articleId))
                    .isInstanceOf(ArticleException.class)
                    .hasMessageContaining("다른 사람의 댓글이 있는 게시글은 삭제할 수 없습니다");
        }

    }

    private User setupUser() {
        User user1 = UserFixture.createUser1();
        userRepository.save(user1);
        return user1;
    }

    private Article setupArticle() {
        Article article = ArticleFixture.createArticle1(fixedDateTime.getNow());
        articleRepository.save(article);
        return article;
    }

}