package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.database.QuestionDatabase;
import com.woowa.database.QuestionMemoryDatabase;
import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.handler.QuestionHandler;
import com.woowa.model.Question;
import com.woowa.model.User;
import com.woowa.support.QuestionFixture;
import com.woowa.support.StubHttpServletRequest;
import com.woowa.support.StubHttpServletResponse;
import com.woowa.support.StubHttpSession;
import com.woowa.support.UserFixture;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindQuestionServletTest {
    private FindQuestionServlet findQuestionServlet;
    private QuestionHandler questionHandler;
    private UserDatabase userDatabase;
    private QuestionDatabase questionDatabase;

    @BeforeEach
    void setUp() {
        questionDatabase = new QuestionMemoryDatabase();
        userDatabase = new UserMemoryDatabase();
        questionHandler = new QuestionHandler(userDatabase, questionDatabase);
        findQuestionServlet = new FindQuestionServlet(questionHandler);
    }

    @Nested
    @DisplayName("doGet 호출 시")
    class DoGetTest {

        private StubHttpServletRequest request;
        private StubHttpServletResponse response;

        @BeforeEach
        void setUp() {
            request = new StubHttpServletRequest();
            response = new StubHttpServletResponse();
        }

        @Test
        @DisplayName("게시글을 반환한다.")
        void test() throws ServletException, IOException {
            //given
            User user = UserFixture.user();
            Question question = QuestionFixture.question(user);

            questionDatabase.save(question);
            request.setRequestUri("/questions/" + question.getQuestionId());

            //when
            findQuestionServlet.doGet(request, response);

            //then
            assertThat(request.getAttribute("question")).isNotNull().isInstanceOf(Question.class);
        }

        @Nested
        @DisplayName("/update 이면")
        class UpdateTest {

            @Test
            @DisplayName("질문 수정 폼을 반환한다.")
            void update() throws ServletException, IOException {
                //given
                User user = UserFixture.user();
                Question question = QuestionFixture.question(user);
                userDatabase.save(user);
                questionDatabase.save(question);

                request.setRequestUri("/questions/" + question.getQuestionId() + "/update");
                StubHttpSession session = new StubHttpSession();
                session.setAttribute("userId", user.getUserId());
                request.setSession(session);

                //when
                findQuestionServlet.doGet(request, response);

                //then
                assertThat(request.getRequestDispatcher().getPath()).isEqualTo("/WEB-INF/classes/static/qna/update.jsp");
            }
        }
    }
}