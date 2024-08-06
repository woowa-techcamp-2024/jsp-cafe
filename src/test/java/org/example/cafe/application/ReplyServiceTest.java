package org.example.cafe.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.cafe.common.exception.BadAuthenticationException;
import org.example.cafe.common.exception.DataNotFoundException;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.ReplyRepository;
import org.example.cafe.infra.QuestionJdbcRepository;
import org.example.cafe.infra.ReplyJdbcRepository;
import org.example.cafe.infra.database.DbConnector;
import org.example.cafe.infra.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("ReplyService 테스트")
class ReplyServiceTest {

    private static final DbConnector dbConnector = new DbConnector().init();
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnector.getDataSource());
    private static final QuestionRepository questionRepository = new QuestionJdbcRepository(jdbcTemplate);
    private static final ReplyRepository replyRepository = new ReplyJdbcRepository(jdbcTemplate);
    private static final ReplyService replyService = new ReplyService(replyRepository);

    private static Question question;

    @BeforeAll
    static void beforeAll() {
        Question questionEntity = new Question("title", "content", "testUser");
        question = questionRepository.findById(questionRepository.save(questionEntity));
    }

    @BeforeEach
    void setUp() {
        replyRepository.deleteAll();
    }

    @Nested
    class 댓글을_삭제한다 {

        @Nested
        class 성공 {
            @Test
            void test_successful_delete_reply() {
                // Given
                Reply reply = new Reply.ReplyBuilder()
                        .writer("user1")
                        .content("Test content")
                        .questionId(question.getQuestionId())
                        .build();
                Long savedId = replyRepository.save(reply);

                // When
                replyService.deleteReply(savedId, "user1");

                // Then
                assertThat(replyRepository.findById(savedId)).isNull();
            }
        }

        @Nested
        class 실패 {

            @Test
            void test_throw_DataNotFoundException_when_reply_not_exist() {
                // When & Then
                assertThrows(DataNotFoundException.class, () -> {
                    replyService.deleteReply(0L, "user3");
                });
            }

            @Test
            void test_throw_BadAuthenticationException_when_user_not_writer() {
                // Given
                Reply reply = new Reply.ReplyBuilder()
                        .writer("user4")
                        .content("Content for user4")
                        .questionId(question.getQuestionId())
                        .build();
                Long saveId = replyRepository.save(reply);

                // When & Then
                assertThrows(BadAuthenticationException.class, () -> {
                    replyService.deleteReply(saveId, "user5");
                });
            }
        }
    }
}
