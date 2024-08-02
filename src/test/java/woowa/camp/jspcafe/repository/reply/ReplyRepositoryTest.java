package woowa.camp.jspcafe.repository.reply;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.fixture.UserFixture;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.repository.UserDBSetupExtension;
import woowa.camp.jspcafe.repository.user.DBUserRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.repository.dto.response.ReplyResponse;
import woowa.camp.jspcafe.utils.FixedDateTimeProvider;

@ExtendWith(ReplyDBSetupExtension.class)
@ExtendWith(UserDBSetupExtension.class)
class ReplyRepositoryTest {

    DatabaseConnector connector = new DatabaseConnector();
    ReplyRepository repository = new DBReplyRepository(connector);
    FixedDateTimeProvider fixedDateTime = new FixedDateTimeProvider(2024, 7, 31);

    @Nested
    @DisplayName("Describe_댓글을 저장하는 기능은")
    class SaveTest {

        @Test
        @DisplayName("[Success] 유효한 댓글 정보로 저장에 성공한다")
        void saveValidReply() {
            // given
            Reply reply = new Reply(1L, 1L, "Test Comment",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);
            // when
            Reply savedReply = repository.save(reply);
            // then
            assertThat(savedReply.getReplyId()).isNotNull();
            assertThat(savedReply)
                    .usingRecursiveComparison()
                    .isEqualTo(reply);
        }

        @Test
        @DisplayName("[Exception] 유효하지 않은 사용자 ID로 저장 시 예외가 발생한다")
        void saveWithInvalidUserId() {
            // given
            Reply reply = new Reply(null, 1L, "Test Comment",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);

            // when & then
            assertThatThrownBy(() -> repository.save(reply))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("[Exception] 유효하지 않은 게시글 ID로 저장 시 예외가 발생한다")
        void saveWithInvalidArticleId() {
            // given
            Reply reply = new Reply(1L, null, "Test Comment",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);

            // when & then
            assertThatThrownBy(() -> repository.save(reply))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("[Exception] 내용이 없는 댓글 저장 시 예외가 발생한다")
        void saveWithEmptyContent() {
            // given
            Reply reply = new Reply(1L, null, "",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);

            // when & then
            assertThatThrownBy(() -> repository.save(reply))
                    .isInstanceOf(RuntimeException.class);
        }

    }

    @Nested
    @DisplayName("Describe_ID기준으로 댓글을 조회하는 기능은")
    class FindByIdTest {

        @Test
        @DisplayName("[Success] 존재하는 ID로 댓글을 성공적으로 조회한다")
        void findExistingReply() {
            // given
            Reply reply = new Reply(1L, 1L, "Test Comment",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);
            Reply savedReply = repository.save(reply);

            // when
            Optional<Reply> foundReply = repository.findById(savedReply.getReplyId());

            // then
            assertThat(foundReply)
                    .isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedReply);
        }

        @Test
        @DisplayName("[Success] 존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
        void findNonExistingReply() {
            // given
            Long nonExistingId = 9999L;

            // when
            Optional<Reply> foundReply = repository.findById(nonExistingId);

            // then
            assertThat(foundReply).isEmpty();
        }

        @Test
        @DisplayName("[Success] 음수 ID로 조회 시 예외가 발생하지 않는다")
        void findReplyWithNegativeId() {
            // given
            Long negativeId = -1L;

            // when & then
            assertThatCode(() -> repository.findById(negativeId))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("[Exception] 유효하지 않은 ID로 조회 시 예외가 발생하지 않는다")
        void findReplyWithNullId() {
            // given
            Long negativeId = null;

            // when & then
            assertThatThrownBy(() -> repository.findById(negativeId))
                    .isInstanceOf(RuntimeException.class);
        }

    }

    @Nested
    @DisplayName("Describe_게시글ID기준으로 댓글을 조회하는 기능은")
    class FindByArticleIdTest {

        @Test
        @DisplayName("[Success] 존재하는 게시글 ID로 댓글 목록을 성공적으로 조회한다")
        void findRepliesForExistingArticle() {
            // given
            Long articleId = 1L;
            Reply reply1 = new Reply(1L, 1L, "Test Comment 1",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);
            Reply reply2 = new Reply(2L, 1L, "Test Comment 1",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);
            repository.save(reply1);
            repository.save(reply2);

            // when
            List<Reply> replies = repository.findByArticleId(articleId);

            // then
            assertThat(replies)
                    .hasSize(2)
                    .allMatch(r -> r.getArticleId().equals(articleId));
        }

        @Test
        @DisplayName("[Success] 존재하지 않는 게시글 ID로 조회 시 빈 리스트를 반환한다")
        void findRepliesForNonExistingArticle() {
            // given
            Long nonExistingArticleId = 9999L;

            // when
            List<Reply> replies = repository.findByArticleId(nonExistingArticleId);

            // then
            assertThat(replies).isEmpty();
        }

        @Test
        @DisplayName("[Success] 음수 게시글 ID로 조회 시 빈 리스트를 반환한다")
        void findRepliesWithNegativeArticleId() {
            // given
            Long negativeArticleId = -1L;

            // when & then
            List<Reply> result = repository.findByArticleId(negativeArticleId);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("[Exception] 유효하지 않은 게시글 ID로 조회 시 예외를 반환한다")
        void findRepliesWithNullArticleId() {
            // given
            Long negativeArticleId = null;

            // when & then
            assertThatThrownBy(() -> repository.findByArticleId(negativeArticleId))
                    .isInstanceOf(RuntimeException.class);
        }

    }

    @Nested
    @DisplayName("Describe_회원ID기준으로 댓글을 소프트 삭제하는 기능은")
    class SoftDeleteByUserIdTest {

        @Test
        @DisplayName("[Success] 존재하는 회원 ID로 댓글을 성공적으로 소프트 삭제한다")
        void softDeleteRepliesForExistingUser() {
            // given
            Long userId1 = 1L;
            Long userId2 = 2L;
            Reply reply1 = new Reply(userId1, 1L, "Test Comment 1",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);
            Reply reply2 = new Reply(userId2, 1L, "Test Comment 1",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);
            repository.save(reply1);
            repository.save(reply2);

            // when
            LocalDateTime deletedTime = fixedDateTime.getNowAsLocalDateTime();
            repository.softDeleteByUserId(userId1, deletedTime);
            repository.softDeleteByUserId(userId2, deletedTime);

            // then
            List<Reply> repliesForArticle1 = repository.findByArticleId(1L);
            assertThat(repliesForArticle1).isEmpty();
        }

        @Test
        @DisplayName("[Success] 존재하지 않는 회원 ID로 소프트 삭제 시 예외가 발생하지 않는다")
        void softDeleteRepliesForNonExistingUser() {
            // given
            Long nonExistingUserId = 9999L;

            // when & then
            assertThatCode(() -> repository.softDeleteByUserId(nonExistingUserId, LocalDateTime.now()))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("[Success] 음수 회원 ID로 소프트 삭제 시 예외가 발생하지 않는다")
        void softDeleteRepliesWithNegativeUserId() {
            // given
            Long negativeUserId = -1L;

            // when & then
            assertThatCode(() -> repository.softDeleteByUserId(negativeUserId, LocalDateTime.now()))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("[Exception] 유효하지 않은 사용자 ID로 소프트 삭제 시 예외가 발생한다")
        void softDeleteRepliesWithNullUserId() {
            // given
            Long negativeUserId = null;

            // when & then
            assertThatThrownBy(() -> repository.softDeleteByUserId(negativeUserId, LocalDateTime.now()))
                    .isInstanceOf(RuntimeException.class);
        }

//        서비스 레이어에서 테스트 해야 함
//        @Test
//        @DisplayName("[Exception] 과거 시간으로 소프트 삭제 시 예외가 발생한다")
//        void softDeleteRepliesWithPastTime() {
//            // given
//            Long userId = 1L;
//            LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
//
//            // when & then
//            assertThatThrownBy(() -> repository.softDeleteByUserId(userId, pastTime))
//                    .isInstanceOf(RuntimeException.class);
//        }

    }

    @Nested
    @DisplayName("Describe_회원테이블과 Join하여 조회하는 기능은")
    class FindByArticleIdWithUserTest {

        UserRepository userRepository = new DBUserRepository(connector);

        @Test
        @DisplayName("[Success] 특정 게시글의 댓글을 사용자 정보와 함께 조회한다")
        void test() {
            // given
            User user1 = UserFixture.createUser(1, fixedDateTime.getNow());
            User user2 = UserFixture.createUser(2, fixedDateTime.getNow());
            Long userId1 = userRepository.save(user1);
            Long userId2 = userRepository.save(user2);

            Reply reply1 = new Reply(userId1, 1L, "Test Comment 1",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);
            Reply reply2 = new Reply(userId2, 1L, "Test Comment 2",
                    fixedDateTime.getNowAsLocalDateTime(),
                    fixedDateTime.getNowAsLocalDateTime(),
                    null);
            repository.save(reply1);
            repository.save(reply2);
            // when
            List<ReplyResponse> result = repository.findByArticleIdWithUser(1L);
            // then
            assertThat(result)
                    .hasSize(2)
                    .satisfies(replies -> {
                        assertThat(replies).extracting(
                                ReplyResponse::getReplyId,
                                ReplyResponse::getUserId,
                                ReplyResponse::getContent,
                                ReplyResponse::getUserNickname,
                                ReplyResponse::getCreatedAt
                        ).containsExactly(
                                tuple(reply1.getReplyId(), userId1, "Test Comment 1", user1.getNickname(),
                                        fixedDateTime.getNowAsLocalDateTime().toString()),
                                tuple(reply2.getReplyId(), userId2, "Test Comment 2", user2.getNickname(),
                                        fixedDateTime.getNowAsLocalDateTime().toString())
                        );
                    });
        }
    }

}