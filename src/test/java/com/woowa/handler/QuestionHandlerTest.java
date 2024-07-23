package com.woowa.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.*;

import com.woowa.database.QuestionDatabase;
import com.woowa.database.QuestionMemoryDatabase;
import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.Author;
import com.woowa.model.Question;
import com.woowa.model.User;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QuestionHandlerTest {

    private QuestionHandler questionHandler;
    private UserDatabase userDatabase;
    private QuestionDatabase questionDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        questionDatabase = new QuestionMemoryDatabase();
        questionHandler = new QuestionHandler(userDatabase, questionDatabase);
    }

    @Nested
    @DisplayName("createQuestion 호출 시")
    class CreateQuestionTest {

        @Test
        @DisplayName("질문을 생성한다.")
        void createQuestion() {
            //given
            User user = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            String title = "title";
            String content = "content";

            userDatabase.save(user);

            //when
            ResponseEntity response = questionHandler.createQuestion(user.getUserId(), title, content);

            //then
            List<Question> result = questionDatabase.findAll();
            assertThat(result).hasSize(1).first().satisfies(question -> {
                assertThat(question.getTitle()).isEqualTo(title);
                assertThat(question.getContent()).isEqualTo(content);
                assertThat(question.getAuthor()).isEqualTo(Author.from(user));
            });
        }

        @Test
        @DisplayName("메인 화면으로 리다이렉트한다.")
        void redirectToMain() {
            //given
            User user = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            String title = "title";
            String content = "content";

            userDatabase.save(user);

            //when
            ResponseEntity response = questionHandler.createQuestion(user.getUserId(), title, content);

            //then
            assertThat(response.getStatus()).isEqualTo(302);
            assertThat(response.getLocation()).isEqualTo("/");
        }

        @Test
        @DisplayName("예외(NoSuchElement): 존재하지 않는 사용자이면")
        void noSuchElement_whenUserNotFound() {
            //given
            String userId = "nope";
            String title = "title";
            String content = "content";

            //when
            Exception exception = catchException(() -> questionHandler.createQuestion(userId, title, content));

            //then
            assertThat(exception).isInstanceOf(NoSuchElementException.class);
        }
    }
}
