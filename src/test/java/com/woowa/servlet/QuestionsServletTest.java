package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.database.question.QuestionDatabase;
import com.woowa.database.question.QuestionMemoryDatabase;
import com.woowa.database.user.UserDatabase;
import com.woowa.database.user.UserMemoryDatabase;
import com.woowa.handler.QuestionHandler;
import com.woowa.model.User;
import com.woowa.support.StubHttpServletRequest;
import com.woowa.support.StubHttpServletResponse;
import com.woowa.support.StubHttpSession;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QuestionsServletTest {

    private QuestionsServlet questionsServlet;
    private QuestionHandler questionHandler;
    private UserDatabase userDatabase;
    private QuestionDatabase questionDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        questionDatabase = new QuestionMemoryDatabase();
        questionHandler = new QuestionHandler(userDatabase, questionDatabase);
        questionsServlet = new QuestionsServlet(questionHandler);
    }

    @Nested
    @DisplayName("doPost 호출 시")
    class DoPostTest {

        private StubHttpServletRequest request;
        private StubHttpServletResponse response;
        private StubHttpSession session;

        @BeforeEach
        void setUp() {
            request = new StubHttpServletRequest();
            response = new StubHttpServletResponse();
            session = new StubHttpSession();
        }

        @Test
        @DisplayName("메인 화면으로 리다이렉트한다.")
        void redirectToMain() throws ServletException, IOException {
            //given
            request.setSession(session);

            String userId = UUID.randomUUID().toString();
            session.setAttribute("userId", userId);
            request.addParameter("title", "title");
            request.addParameter("content", "content");

            userDatabase.save(User.create(userId, "test@test.com", "password", "nickname"));

            //when
            questionsServlet.doPost(request, response);

            //then
            String redirectLocation = response.getRedirectLocation();
            assertThat(redirectLocation).isEqualTo("/");
        }

        @Test
        @DisplayName("세션이 없으면 로그인 화면으로 리다이렉트한다.")
        void redirectToLogin_whenNoSession() throws ServletException, IOException {
            //given

            //when
            questionsServlet.doPost(request, response);

            //then
            String redirectLocation = response.getRedirectLocation();
            assertThat(redirectLocation).isEqualTo("/user/login.jsp");
        }
    }
}