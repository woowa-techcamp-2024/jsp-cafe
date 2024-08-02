package woowa.camp.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.domain.exception.ReplyException;
import woowa.camp.jspcafe.domain.exception.UnAuthorizationException;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.fixture.UserFixture;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.repository.ArticleDBSetupExtension;
import woowa.camp.jspcafe.repository.UserDBSetupExtension;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.article.DBArticleRepository;
import woowa.camp.jspcafe.repository.reply.DBReplyRepository;
import woowa.camp.jspcafe.repository.reply.ReplyDBSetupExtension;
import woowa.camp.jspcafe.repository.reply.ReplyRepository;
import woowa.camp.jspcafe.repository.user.DBUserRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.ReplyResponse;
import woowa.camp.jspcafe.service.dto.ReplyWriteRequest;
import woowa.camp.jspcafe.utils.FixedDateTimeProvider;

@ExtendWith({ReplyDBSetupExtension.class, UserDBSetupExtension.class, ArticleDBSetupExtension.class})
class ReplyServiceTest {

    DateTimeProvider fixedDateTime = new FixedDateTimeProvider(2024, 8, 1);

    DatabaseConnector connector = new DatabaseConnector();
    UserRepository userRepository = new DBUserRepository(connector);
    ArticleRepository articleRepository = new DBArticleRepository(connector);
    ReplyRepository replyRepository = new DBReplyRepository(connector);

    ReplyService replyService = new ReplyService(replyRepository, userRepository, fixedDateTime);

    @Nested
    @DisplayName("Describe_댓글을 작성하는 기능은")
    class WriteReplyTest {

        @Test
        @DisplayName("[Success] 생성/수정 날짜는 현재 시간으로 동일하며, 삭제 날짜는 값이 없다")
        void success() {
            // given
            Reply reply = replyService.writeReply(new ReplyWriteRequest(1L, 2L, "댓글 내용"));
            // when

            // then
            assertThat(reply.getCreatedAt()).isEqualTo(fixedDateTime.getNowAsLocalDateTime());
            assertThat(reply.getUpdatedAt()).isEqualTo(fixedDateTime.getNowAsLocalDateTime());
            assertThat(reply.getDeletedAt()).isNull();
            assertThat(reply.getUserId()).isEqualTo(1L);
            assertThat(reply.getArticleId()).isEqualTo(2L);
            assertThat(reply.getContent()).isEqualTo("댓글 내용");
        }
    }

    @Nested
    @DisplayName("Describe_댓글 목록을 조회하는 기능은")
    class FindReplyListTest {

        @Test
        @DisplayName("[Success] 게시글 ID 기준으로 댓글 작성자 정보와 함께 조회한다")
        void test() {
            // given
            User replier = new User("email@email.com", "nickname", "password", fixedDateTime.getNow());
            Long replierId = userRepository.save(replier);

            Article article = new Article(replier.getId(), "게시글 제목", "게시글 내용", 1, fixedDateTime.getNow(),
                    fixedDateTime.getNow());
            Long articleId = articleRepository.save(article);

            Reply reply = new Reply(replierId, articleId, "댓글 내용", fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(), null);
            replyRepository.save(reply);

            // when
            List<ReplyResponse> result = replyService.findReplyList(articleId);

            // then
            assertThat(result.size()).isEqualTo(1);
            ReplyResponse replyResponse = result.get(0);
            assertReplyResponse(replyResponse, reply, replier);
        }

        private void assertReplyResponse(ReplyResponse replyResponse, Reply reply, User replier) {
            assertThat(replyResponse.getReplyId()).isEqualTo(reply.getReplyId());
            assertThat(replyResponse.getCreatedAt()).isEqualTo(reply.getCreatedAt());
            assertThat(replyResponse.getContent()).isEqualTo(reply.getContent());
            assertThat(replyResponse.getUserId()).isEqualTo(reply.getUserId());
            assertThat(replyResponse.getUserNickname()).isEqualTo(replier.getNickname());
        }

        @Test
        @DisplayName("[Success] 게시글에 작성된 댓글 개수만큼 조회한다")
        void test3() {
            // given
            Long articleId = 1L;
            Long count = 30L;
            List<User> users = UserFixture.createMultipleUsers(30, fixedDateTime.getNow());
            for (User user : users) {
                userRepository.save(user);
            }
            List<Reply> replies = createMultipleReplies(count, articleId, fixedDateTime.getNowAsLocalDateTime());
            for (Reply reply : replies) {
                replyRepository.save(reply);
            }
            // when
            List<ReplyResponse> replyList = replyService.findReplyList(articleId);
            // then
            assertThat(replyList.size()).isEqualTo(30);
        }

        @Test
        @DisplayName("[Success] 댓글 작성자에 해당하는 사용자가 없으면 조회하지 않는다")
        void test4() {
            // given
            Long articleId = 1L;
            Long count = 30L;
            List<Reply> replies = createMultipleReplies(count, articleId, fixedDateTime.getNowAsLocalDateTime());
            for (Reply reply : replies) {
                replyRepository.save(reply);
            }

            // when
            List<ReplyResponse> replyList = replyService.findReplyList(articleId);

            // then
            assertThat(replyList.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("[Success] 없는 게시글 ID로 조회할 경우 빈 리스트를 반환한다")
        void test2() {
            // given
            User replier = new User("email@email.com", "nickname", "password", fixedDateTime.getNow());
            Long replierId = userRepository.save(replier);

            Article article = new Article(replier.getId(), "게시글 제목", "게시글 내용", 1, fixedDateTime.getNow(),
                    fixedDateTime.getNow());
            Long articleId = articleRepository.save(article);

            Reply reply = new Reply(replierId, articleId, "댓글 내용", fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(), null);
            replyRepository.save(reply);

            // when
            List<ReplyResponse> result = replyService.findReplyList(123456L);

            // then
            assertThat(result).isEmpty();
        }

        public List<Reply> createMultipleReplies(Long count, Long articleId, LocalDateTime now) {
            return LongStream.rangeClosed(1, count)
                    .mapToObj(i -> createReply(count, articleId, now))
                    .toList();
        }

        public Reply createReply(Long number, Long articleId, LocalDateTime now) {
            return new Reply(number, articleId, "댓글 내용", now, now, null);
        }

    }

    @Nested
    @DisplayName("Describe_댓글을 삭제하는 기능은")
    class DeleteReplyTest {

        @Test
        @DisplayName("[Success] 정상적으로 댓글을 삭제한다")
        void deleteReplySuccessfully() {
            // given
            User user = new User("user@email.com", "nickname", "password", fixedDateTime.getNow());
            Long userId = userRepository.save(user);

            Article article = new Article(userId, "제목", "내용", 0, fixedDateTime.getNow(), fixedDateTime.getNow());
            Long articleId = articleRepository.save(article);

            Reply reply = new Reply(userId, articleId, "댓글 내용", fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(), null);
            Reply saveReply = replyRepository.save(reply);

            // when
            replyService.deleteReply(user, articleId, saveReply.getReplyId());

            // then
            assertThat(replyRepository.findById(saveReply.getReplyId())).isEmpty();
        }

        @Test
        @DisplayName("[Exception] 존재하지 않는 댓글ID면 ReplyException 예외가 발생한다")
        void throwReplyExceptionForNonExistentReply() {
            // given
            User user = new User("user@email.com", "nickname", "password", fixedDateTime.getNow());
            userRepository.save(user);

            Long nonExistentReplyId = 9999L;

            // when & then
            assertThatThrownBy(() -> replyService.deleteReply(user, 1L, nonExistentReplyId))
                    .isInstanceOf(ReplyException.class)
                    .hasMessageContaining("Reply not found");
        }

        @Test
        @DisplayName("[Exception] 댓글 작성자의 회원ID가 존재하지 않으면 UserException 예외가 발생한다")
        void throwUserExceptionForNonExistentUser() {
            // given
            User user = new User("user@email.com", "nickname", "password", fixedDateTime.getNow());
            Long userId = userRepository.save(user);

            Article article = new Article(userId, "제목", "내용", 0, fixedDateTime.getNow(), fixedDateTime.getNow());
            Long articleId = articleRepository.save(article);

            Reply reply = new Reply(9999L, articleId, "댓글 내용", fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(), null);
            Reply saveReply = replyRepository.save(reply);

            // when & then
            assertThatThrownBy(() -> replyService.deleteReply(user, articleId, saveReply.getReplyId()))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("User with id 9999 not found");
        }

        @Test
        @DisplayName("[Exception] 댓글을 삭제하려는 회원과 댓글 작성자의 이메일이 일치하지 않으면 UnAuthorizationException 예외가 발생한다")
        void throwUnAuthorizationExceptionForUnauthorizedUser() {
            // given
            User author = new User("author@email.com", "author", "password", fixedDateTime.getNow());
            Long authorId = userRepository.save(author);

            User unauthorizedUser = new User("unauthorized@email.com", "unauthorized", "password",
                    fixedDateTime.getNow());
            userRepository.save(unauthorizedUser);

            Article article = new Article(authorId, "제목", "내용", 0, fixedDateTime.getNow(), fixedDateTime.getNow());
            Long articleId = articleRepository.save(article);

            Reply reply = new Reply(authorId, articleId, "댓글 내용", fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(), null);
            Reply saveReply = replyRepository.save(reply);

            // when & then
            assertThatThrownBy(() -> replyService.deleteReply(unauthorizedUser, articleId, saveReply.getReplyId()))
                    .isInstanceOf(UnAuthorizationException.class)
                    .hasMessageContaining("게시글 수정은 작성자의 이메일과 동일해야 합니다");
        }

        @Test
        @DisplayName("[Exception] 요청으로 받은 게시글 ID와 댓글의 게시글 ID가 일치하지 않으면 ArticleException 예외가 발생한다")
        void throwArticleExceptionForMismatchedArticleId() {
            // given
            User user = new User("user@email.com", "nickname", "password", fixedDateTime.getNow());
            Long userId = userRepository.save(user);

            Article article = new Article(userId, "제목", "내용", 0, fixedDateTime.getNow(), fixedDateTime.getNow());
            Long articleId = articleRepository.save(article);

            Reply reply = new Reply(userId, articleId, "댓글 내용", fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(), null);
            Reply saveReply = replyRepository.save(reply);

            Long mismatchedArticleId = articleId + 1;

            // when & then
            assertThatThrownBy(() -> replyService.deleteReply(user, mismatchedArticleId, saveReply.getReplyId()))
                    .isInstanceOf(ArticleException.class)
                    .hasMessageContaining("편집하려는 댓글의 게시글 id와 일치하지 않습니다");
        }

    }

}