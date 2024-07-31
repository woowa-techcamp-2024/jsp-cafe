package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.woowa.database.QuestionDatabase;
import com.woowa.database.QuestionMemoryDatabase;
import com.woowa.database.ReplyDatabase;
import com.woowa.database.ReplyMemoryDatabase;
import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.exception.AuthenticationException;
import com.woowa.handler.QuestionHandler;
import com.woowa.handler.ReplyHandler;
import com.woowa.model.Question;
import com.woowa.model.Reply;
import com.woowa.model.User;
import com.woowa.support.QuestionFixture;
import com.woowa.support.ReplyFixture;
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
    private ReplyHandler replyHandler;
    private UserDatabase userDatabase;
    private QuestionDatabase questionDatabase;
    private ReplyDatabase replyDatabase;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        questionDatabase = new QuestionMemoryDatabase();
        userDatabase = new UserMemoryDatabase();
        questionHandler = new QuestionHandler(userDatabase, questionDatabase);
        replyDatabase = new ReplyMemoryDatabase();
        replyHandler = new ReplyHandler(userDatabase, questionDatabase, replyDatabase);
        findQuestionServlet = new FindQuestionServlet(questionHandler, replyHandler, objectMapper);
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
        void setAttributeQuestion() throws ServletException, IOException {
            //given
            User user = UserFixture.user();
            Question question = QuestionFixture.question(user);
            questionDatabase.save(question);
            userDatabase.save(user);
            StubHttpSession session = new StubHttpSession();
            session.setAttribute("userId", user.getUserId());
            request.setSession(session);
            request.setRequestUri("/questions/" + question.getQuestionId());

            //when
            findQuestionServlet.doGet(request, response);

            //then
            assertThat(request.getAttribute("question")).isNotNull().isInstanceOf(Question.class);
        }

        @Test
        @DisplayName("예외(Authentication): 로그인하지 않았으면")
        void authentication_WhenNoLogin() throws ServletException, IOException {
            //given
            User user = UserFixture.user();
            Question question = QuestionFixture.question(user);
            questionDatabase.save(question);
            userDatabase.save(user);

            request.setRequestUri("/questions/" + question.getQuestionId());

            //when
            Exception exception = catchException(() -> findQuestionServlet.doGet(request, response));

            //then
            assertThat(exception).isInstanceOf(AuthenticationException.class);
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

    @Nested
    @DisplayName("DoPost 호출 시")
    class DoPostTest {

        private StubHttpServletRequest request;
        private StubHttpServletResponse response;
        private User user;
        private Question question;

        @BeforeEach
        void setUp() {
            request = new StubHttpServletRequest();
            response = new StubHttpServletResponse();
            user = UserFixture.user();
            question = QuestionFixture.question(user);
            userDatabase.save(user);
            questionDatabase.save(question);
            request.setRequestUri("/questions/" + question.getQuestionId() + "/replies");
        }

        @Test
        @DisplayName("질문 상세 조회로 리다이렉트한다.")
        void redirectToQuestionDetail() throws ServletException, IOException {
            //given
            StubHttpSession session = new StubHttpSession();
            session.setAttribute("userId", user.getUserId());
            request.setSession(session);
            request.addParameter("content", "content");

            //when
            findQuestionServlet.doPost(request, response);

            //then
            assertThat(response.getRedirectLocation()).isEqualTo("/questions/" + question.getQuestionId());
        }

        @Test
        @DisplayName("예외(Authentication): 로그인 되어 있지 않으면")
        void authentication_WhenNoLogin() throws ServletException, IOException {
            //given
            request.addParameter("content", "content");

            //when
            Exception exception = catchException(() -> findQuestionServlet.doPost(request, response));

            //then
            assertThat(exception).isInstanceOf(AuthenticationException.class);
        }
    }

    @Nested
    @DisplayName("doPut 호출 시")
    class DoPutTest {

        private StubHttpServletRequest request;
        private StubHttpServletResponse response;
        private User user;
        private Question question;

        @BeforeEach
        void setUp() {
            request = new StubHttpServletRequest();
            response = new StubHttpServletResponse();
            StubHttpSession session = new StubHttpSession();
            request.setSession(session);

            user = UserFixture.user();
            question = QuestionFixture.question(user);
            userDatabase.save(user);
            questionDatabase.save(question);
            session.setAttribute("userId", user.getUserId());
        }

        @Test
        @DisplayName("질문 상세 조회로 리다이렉트한다.")
        void redirectToQuestion() throws ServletException, IOException {
            //given
            request.setRequestUri("/questions/" + question.getQuestionId());
            request.addParameter("title", "updateTitle");
            request.addParameter("content", "updateContent");

            //when
            findQuestionServlet.doPut(request, response);

            //then
            assertThat(response.getRedirectLocation()).isEqualTo("/questions/" + question.getQuestionId());
        }
    }

    @Nested
    @DisplayName("DoDelete 호출 시")
    class DoDeleteTest {

        private StubHttpServletRequest request;
        private StubHttpServletResponse response;

        @BeforeEach
        void setUp() {
            request = new StubHttpServletRequest();
            response = new StubHttpServletResponse();
        }

        @Nested
        @DisplayName("/questions/{questionId}이면")
        class DeleteQuestionPath {

            @Test
            @DisplayName("질문 리스트 화면으로 리다이렉트한다.")
            void returnToListQuestions() throws ServletException, IOException {
                //given
                User user = UserFixture.user();
                Question question = QuestionFixture.question(user);
                userDatabase.save(user);
                questionDatabase.save(question);
                StubHttpSession session = new StubHttpSession();
                session.setAttribute("userId", user.getUserId());
                request.setSession(session);
                request.setRequestUri("/questions/" + question.getQuestionId());

                //when
                findQuestionServlet.doDelete(request, response);

                //then
                assertThat(response.getRedirectLocation()).isEqualTo("/");
            }
        }

        @Nested
        @DisplayName("/questions/{questionId}/replies/{replyId}이면")
        class DeleteReplyPath {

            private User user;
            private Question question;
            private Reply reply;

            @BeforeEach
            void setUp() {
                user = UserFixture.user();
                question = QuestionFixture.question(user);
                reply = ReplyFixture.reply(user, question);
                userDatabase.save(user);
                questionDatabase.save(question);
                replyDatabase.save(reply);

                request.setRequestUri("/questions/" + question.getQuestionId() + "/replies/" + reply.getReplyId());
            }

            @Test
            @DisplayName("질문 상세 화면으로 리다이렉트한다.")
            void redirectToQuestionDetail() throws ServletException, IOException {
                //given
                StubHttpSession session = new StubHttpSession();
                session.setAttribute("userId", user.getUserId());
                request.setSession(session);

                //when
                findQuestionServlet.doDelete(request, response);

                //then
                assertThat(response.getRedirectLocation()).isEqualTo("/questions/" + question.getQuestionId());
            }

            @Test
            @DisplayName("예외(Authentication): 로그인되어 있지 않으면")
            void authentication_WhenNoLogin() throws ServletException, IOException {
                //given

                //when
                Exception exception = catchException(() -> findQuestionServlet.doDelete(request, response));

                //then
                assertThat(exception).isInstanceOf(AuthenticationException.class);
            }
        }
    }
}