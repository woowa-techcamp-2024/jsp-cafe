package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.*;

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
import com.woowa.support.UserFixture;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ListQuestionServletTest {

    private QuestionHandler questionHandler;
    private ListQuestionServlet listQuestionServlet;
    private QuestionDatabase questionDatabase;
    private UserDatabase userDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        questionDatabase = new QuestionMemoryDatabase();
        questionHandler = new QuestionHandler(userDatabase, questionDatabase);
        listQuestionServlet = new ListQuestionServlet(questionHandler);
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
        @DisplayName("page, size 파라미터가 없는 경우 0, 10 을 기본값으로 한다.")
        void defaultPage_0_Size_10() throws ServletException, IOException {
            //given
            User user = UserFixture.user();
            for(int i=0; i<20; i++) {
                Question question = QuestionFixture.question(user);
                questionDatabase.save(question);
            }

            //when
            listQuestionServlet.doGet(request, response);

            //then
            Object questions = request.getAttribute("questions");
            assertThat(questions).isNotNull()
                    .asInstanceOf(LIST)
                    .hasSize(10);
        }

        @Test
        @DisplayName("page, size 파라미터가 있는 경우 해당 값을 사용한다.")
        void usingPageSizeParameter() throws ServletException, IOException {
            //given
            User user = UserFixture.user();
            for(int i=0; i<20; i++) {
                Question question = QuestionFixture.question(user);
                questionDatabase.save(question);
            }

            request.addParameter("page", "1");
            request.addParameter("size", "15");

            //when
            listQuestionServlet.doGet(request, response);

            //then
            Object questions = request.getAttribute("questions");
            assertThat(questions).isNotNull()
                    .asInstanceOf(LIST)
                    .hasSize(5);
        }
    }
}