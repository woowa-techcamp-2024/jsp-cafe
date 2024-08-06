package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.woowa.database.Page;
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
import com.woowa.support.StubPrintWriter;
import com.woowa.support.UserFixture;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QuestionDetailServletTest {
    private QuestionDetailServlet questionDetailServlet;
    private QuestionHandler questionHandler;
    private ReplyHandler replyHandler;
    private UserDatabase userDatabase;
    private QuestionDatabase questionDatabase;
    private ReplyDatabase replyDatabase;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        questionDatabase = new QuestionMemoryDatabase();
        userDatabase = new UserMemoryDatabase();
        questionHandler = new QuestionHandler(userDatabase, questionDatabase);
        replyDatabase = new ReplyMemoryDatabase();
        replyHandler = new ReplyHandler(userDatabase, questionDatabase, replyDatabase);
        questionDetailServlet = new QuestionDetailServlet(questionHandler, replyHandler, objectMapper);
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
            questionDetailServlet.doGet(request, response);

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
            Exception exception = catchException(() -> questionDetailServlet.doGet(request, response));

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
                questionDetailServlet.doGet(request, response);

                //then
                assertThat(request.getRequestDispatcher().getPath()).isEqualTo("/qna/update.jsp");
            }
        }

        @Nested
        @DisplayName("/replies 이면")
        class FindRepliesTest {

            @Test
            @DisplayName("게시글 댓글 목록을 json으로 응답한다.")
            void returnToJson() throws ServletException, IOException {
                //given
                User user = UserFixture.user();
                Question question = QuestionFixture.question(user);
                List<Reply> replies = ReplyFixture.replies(user, question, 5);

                userDatabase.save(user);
                questionDatabase.save(question);
                for (Reply reply : replies) {
                    replyDatabase.save(reply);
                }
                StubHttpSession session = new StubHttpSession();
                session.setAttribute("userId", user.getUserId());
                request.setSession(session);
                request.setRequestUri("/questions/" + question.getQuestionId() + "/replies");

                //when
                questionDetailServlet.doGet(request, response);

                //then
                String result = ((StubPrintWriter) response.getWriter()).getPrintedValue();
                Page<Reply> replyPage = Page.of(replies, 5L, 0, 5);
                assertThat(result).isEqualTo(objectMapper.writeValueAsString(replyPage));
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
        @DisplayName("json 응답을 출력한다.")
        void redirectToQuestionDetail() throws ServletException, IOException {
            //given
            StubHttpSession session = new StubHttpSession();
            session.setAttribute("userId", user.getUserId());
            request.setSession(session);
            request.addParameter("content", "content");

            //when
            questionDetailServlet.doPost(request, response);

            //then
            Reply reply = replyDatabase.findAll().stream().findFirst().get();
            StubPrintWriter writer = (StubPrintWriter) response.getWriter();
            assertThat(writer.getPrintedValue()).isEqualTo(objectMapper.writeValueAsString(reply));
        }

        @Test
        @DisplayName("예외(Authentication): 로그인 되어 있지 않으면")
        void authentication_WhenNoLogin() throws ServletException, IOException {
            //given
            request.addParameter("content", "content");

            //when
            Exception exception = catchException(() -> questionDetailServlet.doPost(request, response));

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
            questionDetailServlet.doPut(request, response);

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
                questionDetailServlet.doDelete(request, response);

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
            @DisplayName("204 응답을 반환한다.")
            void redirectToQuestionDetail() throws ServletException, IOException {
                //given
                StubHttpSession session = new StubHttpSession();
                session.setAttribute("userId", user.getUserId());
                request.setSession(session);

                //when
                questionDetailServlet.doDelete(request, response);

                //then
                assertThat(response.getStatus()).isEqualTo(204);
            }

            @Test
            @DisplayName("예외(Authentication): 로그인되어 있지 않으면")
            void authentication_WhenNoLogin() throws ServletException, IOException {
                //given

                //when
                Exception exception = catchException(() -> questionDetailServlet.doDelete(request, response));

                //then
                assertThat(exception).isInstanceOf(AuthenticationException.class);
            }
        }
    }
}