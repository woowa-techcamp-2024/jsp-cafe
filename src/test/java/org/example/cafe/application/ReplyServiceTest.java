package org.example.cafe.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.cafe.fixture.Fixture.createReply;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import org.example.cafe.application.dto.ReplyPageParam;
import org.example.cafe.common.exception.BadAuthenticationException;
import org.example.cafe.common.exception.DataNotFoundException;
import org.example.cafe.common.page.Page;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.ReplyRepository;
import org.example.cafe.infra.QuestionJdbcRepository;
import org.example.cafe.infra.ReplyJdbcRepository;
import org.example.cafe.infra.database.DbConnector;
import org.example.cafe.infra.jdbc.GeneratedKeyHolder;
import org.example.cafe.infra.jdbc.JdbcTemplate;
import org.example.cafe.infra.jdbc.KeyHolder;
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

    private static Long insertWithCreateTime(Reply reply) {
        final String INSERT = "INSERT INTO REPLY (content, writer, question_id, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT, keyHolder, reply.getContent(), reply.getWriter(), reply.getQuestionId(),
                reply.getCreatedAt());

        return keyHolder.getKey();
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

    @Nested
    class 댓글을_페이징하여_조회한다 {

        @Test
        void 댓글을_페이징하여_조회할_수_있다() {
            // Given
            Reply reply1 = createReply("content1", "writer1", question.getQuestionId(), "2024-07-01T00:00:01");
            Reply reply2 = createReply("content2", "writer2", question.getQuestionId(), "2024-07-01T00:00:02");
            Reply reply3 = createReply("content3", "writer3", question.getQuestionId(), "2024-07-01T00:00:03");
            Reply reply4 = createReply("content4", "writer4", question.getQuestionId(), "2024-07-01T00:00:04");
            Reply reply5 = createReply("content5", "writer5", question.getQuestionId(), "2024-07-01T00:00:05");
            Reply reply6 = createReply("content6", "writer6", question.getQuestionId(), "2024-07-01T00:00:06");
            Reply reply7 = createReply("content7", "writer7", question.getQuestionId(), "2024-07-01T00:00:07");
            insertWithCreateTime(reply1);
            Long savedId = insertWithCreateTime(reply2);
            insertWithCreateTime(reply3);
            insertWithCreateTime(reply4);
            insertWithCreateTime(reply5);
            insertWithCreateTime(reply6);
            insertWithCreateTime(reply7);

            ReplyPageParam replyPageParam = new ReplyPageParam(question.getQuestionId(),
                    savedId, LocalDateTime.parse("2024-07-01T00:00:02"));

            // When
            Page<Reply> result = replyService.findReplyPageByQuestionId(replyPageParam);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(5);
            assertThat(result.getContent())
                    .extracting("content")
                    .containsExactlyElementsOf(List.of("content3", "content4", "content5", "content6", "content7"));
        }

        @Test
        void 댓글이_없는_경우_빈_페이지를_반환한다() {
            // Given
            ReplyPageParam replyPageParam = new ReplyPageParam(question.getQuestionId(),
                    null, LocalDateTime.parse("2021-07-01T00:00:00"));

            // When
            Page<Reply> result = replyService.findReplyPageByQuestionId(replyPageParam);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
