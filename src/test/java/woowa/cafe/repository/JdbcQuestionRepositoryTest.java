package woowa.cafe.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import woowa.cafe.domain.Question;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("질문(게시판) 쿼리 테스트")
class JdbcQuestionRepositoryTest {

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("woowa")
            .withUsername("user1")
            .withPassword("test");

    private final JdbcQuestionRepository jdbcQuestionRepository = new JdbcQuestionRepository(
            new TestJdbcConfig(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword())
    );

    @Test
    @Order(1)
    @DisplayName("질문(게시판)을 생성할 수 있다.")
    public void create_question() {
        // given
        Question question = new Question(
                "authorName",
                "title",
                "contents",
                "userId"
        );

        // when
        jdbcQuestionRepository.save(question);

        // then
        List<Question> questions = jdbcQuestionRepository.findAll();
        for (Question q : questions) {
            assertThat(q.getId()).isNotNull();
            assertThat(q.getAuthorName()).isEqualTo("authorName");
            assertThat(q.getTitle()).isEqualTo("title");
            assertThat(q.getContent()).isEqualTo("contents");
            assertThat(q.getUserId()).isEqualTo("userId");
        }
        assertThat(questions).hasSize(1);
    }

    /**
     * 테스트 순서에 따라서 첫 질문 id = 1 이 존재한다.
     */
    @Test
    @Order(2)
    @DisplayName("질문을 수정하면 변경사항이 반영된다.")
    public void update_question() {
        // given
        Question question = jdbcQuestionRepository.findById("1");
        String newTitle = UUID.randomUUID().toString();
        String newContent = UUID.randomUUID().toString();

        // when
        question.setTitle(newTitle);
        question.setContent(newContent);
        jdbcQuestionRepository.update(question);

        // then
        Question updatedQuestion = jdbcQuestionRepository.findById(question.getId());
        assertThat(updatedQuestion.getTitle()).isEqualTo(newTitle);
        assertThat(updatedQuestion.getContent()).isEqualTo(newContent);
    }
}