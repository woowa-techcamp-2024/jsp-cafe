package woowa.cafe.repository;

import org.junit.jupiter.api.*;
import woowa.cafe.domain.Reply;
import woowa.cafe.testContainer.RepositoryTestContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("댓글 저장소 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcReplyRepositoryTest extends RepositoryTestContainer {

    private final JdbcReplyRepository jdbcReplyRepository = new JdbcReplyRepository(
            new TestJdbcConfig(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword())
    );

    @Test
    @DisplayName("댓글을 생성할 수 있다.")
    @Order(1)
    public void createReply() {
        // given
        Reply reply = new Reply(
                "content",
                "ACTIVE",
                "userId",
                "authorName",
                "questionId"
        );

        // when
        jdbcReplyRepository.save(reply);

        // then
        List<Reply> replies = jdbcReplyRepository.findAll();
        for (Reply r : replies) {
            assertAll(() -> {
                assertThat(r.getId()).isNotNull();
                assertThat(r.getContent()).isEqualTo("content");
                assertThat(r.getStatus()).isEqualTo("ACTIVE");
                assertThat(r.getUserId()).isEqualTo("userId");
                assertThat(r.getAuthorName()).isEqualTo("authorName");
                assertThat(r.getQuestionId()).isEqualTo("questionId");
            });
        }
        assertThat(replies).hasSize(1);
    }

    @Test
    @Order(2)
    @DisplayName("삭제하지 않은 댓글을 조회할 수 있다.")
    public void findById() {
        // given
        // 처음 저장한 댓글의 id는 1이다. AUTO_INCREMENT
        String id = "1";

        // when
        Reply findReply = jdbcReplyRepository.findById(id);

        // then
        assertAll(() -> {
            assertThat(findReply.getId()).isNotNull();
            assertThat(findReply.getContent()).isEqualTo("content");
            assertThat(findReply.getStatus()).isEqualTo("ACTIVE");
            assertThat(findReply.getUserId()).isEqualTo("userId");
            assertThat(findReply.getAuthorName()).isEqualTo("authorName");
            assertThat(findReply.getQuestionId()).isEqualTo("questionId");
        });
    }

    @Test
    @Order(3)
    @DisplayName("댓글을 삭제하면 댓글를 조회할 수 없다.")
    public void deleteById() {
        // given
        // 처음 저장한 댓글의 id는 1이다. AUTO_INCREMENT
        String id = "1";

        // when
        jdbcReplyRepository.deleteById(id);

        // then
        Reply findReply = jdbcReplyRepository.findById(id);
        assertThat(findReply).isNull();
    }

    @Test
    @DisplayName("질문글 id에 해당하는 댓글을 일괄 삭제할 수 있다.")
    public void deleteAllByQuestionId() {
        // given
        String questionId = "questionId";
        for (int i = 0; i < 10; i++) {
            Reply reply = new Reply(
                    "content" + i,
                    "ACTIVE",
                    "userId" + i,
                    "authorName" + i,
                    questionId
            );
            jdbcReplyRepository.save(reply);
        }

        // when
        jdbcReplyRepository.deleteByQuestionId(questionId);

        // then
        List<Reply> replies = jdbcReplyRepository.findAll();
        assertThat(replies).isEmpty();
    }

    @Test
    @DisplayName("다른 사람이 질문글에 댓글을 달았는지 확인할 수 있다.")
    public void existsByQuestionIdAndNotUserId() {
        // given
        String questionId = "questionId";
        for (int i = 0; i < 10; i++) {
            Reply reply = new Reply(
                    "content" + i,
                    "ACTIVE",
                    "userId" + i,
                    "authorName" + i,
                    questionId
            );
            jdbcReplyRepository.save(reply);
        }

        // when
        boolean exists = jdbcReplyRepository.existsByQuestionIdAndNotUserId(questionId, "userId");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("질문글에 다른 사람이 댓글을 작성하지 않았다면 false를 반환한다.")
    public void test2(){
        // given
        String questionId = "questionId";
        for (int i = 0; i < 10; i++) {
            Reply reply = new Reply(
                    "content" + i,
                    "ACTIVE",
                    "userId",
                    "authorName" + i,
                    questionId
            );
            jdbcReplyRepository.save(reply);
        }

        // when
        boolean exists = jdbcReplyRepository.existsByQuestionIdAndNotUserId(questionId, "userId");

        // then
        assertThat(exists).isFalse();
    }

}