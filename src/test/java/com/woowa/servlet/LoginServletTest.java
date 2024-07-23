package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.handler.LoginHandler;
import com.woowa.handler.UserHandler;
import com.woowa.model.User;
import com.woowa.support.StubHttpServletRequest;
import com.woowa.support.StubHttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LoginServletTest {

    private LoginServlet loginServlet;
    private LoginHandler loginHandler;
    private UserMemoryDatabase userDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        loginHandler = new LoginHandler(userDatabase);
        loginServlet = new LoginServlet(loginHandler);
    }

    @Nested
    @DisplayName("doPost 호출 시")
    class DoPostTest {

        private StubHttpServletRequest request;
        private StubHttpServletResponse response;

        @BeforeEach
        void setUp() {
            request = new StubHttpServletRequest();
            response = new StubHttpServletResponse();
        }

        @Test
        @DisplayName("세션을 생성한다.")
        void createSession() throws ServletException, IOException {
            //given
            String userId = UUID.randomUUID().toString();
            String email = "test@test.com";
            String password = "password";
            User user = User.create(userId, email, password, "nickname");

            request.addParameter("email", "test@test.com");
            request.addParameter("password", "password");

            userDatabase.save(user);

            //when
            loginServlet.doPost(request, response);

            //then
            HttpSession session = request.getSession(false);
            assertThat(session).isNotNull();
            assertThat(session.getAttribute("userId")).isNotNull().isEqualTo(userId);
        }

        @Test
        @DisplayName("메인 화면으로 리다이렉트한다.")
        void redirectToMain() throws ServletException, IOException {
            //given
            String userId = UUID.randomUUID().toString();
            String email = "test@test.com";
            String password = "password";
            User user = User.create(userId, email, password, "nickname");

            request.addParameter("email", "test@test.com");
            request.addParameter("password", "password");

            userDatabase.save(user);

            //when
            loginServlet.doPost(request, response);

            //then
            String redirectLocation = response.getRedirectLocation();
            assertThat(redirectLocation).isEqualTo("/");
        }
    }
}
