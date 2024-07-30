package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.LoginHandler;
import com.woowa.support.StubHttpServletRequest;
import com.woowa.support.StubHttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LogoutServletTest {

    private UserDatabase userDatabase;
    private LogoutServlet logoutServlet;
    private LoginHandler loginHandler;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        loginHandler = new LoginHandler(userDatabase);
        logoutServlet = new LogoutServlet(loginHandler);
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
        @DisplayName("메인 화면으로 리다이렉트한다.")
        void redirectToMain() throws ServletException, IOException {
            //given
            request.getSession();

            //when
            logoutServlet.doGet(request, response);

            //then
            assertThat(response.getRedirectLocation()).isEqualTo("/");
        }
    }
}