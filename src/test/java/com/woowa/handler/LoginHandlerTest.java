package com.woowa.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.woowa.database.user.UserDatabase;
import com.woowa.database.user.UserMemoryDatabase;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.User;
import com.woowa.support.StubHttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoginHandlerTest {

    private static final Logger log = LoggerFactory.getLogger(LoginHandlerTest.class);
    private LoginHandler loginHandler;
    private UserDatabase userDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        loginHandler = new LoginHandler(userDatabase);
    }

    @Nested
    @DisplayName("login 호출 시")
    class LoginTest {

        @Test
        @DisplayName("세션을 생성한다.")
        void createSession() {
            //given
            String userId = UUID.randomUUID().toString();
            String email = "test@test.com";
            String password = "password";
            User user = User.create(userId, email, password, "test");
            StubHttpServletRequest request = new StubHttpServletRequest();

            userDatabase.save(user);

            //when
            ResponseEntity response = loginHandler.login(email, password, request);

            //then
            HttpSession session = request.getSession();
            assertThat(session).isNotNull();
            assertThat(session.getAttribute("userId")).isNotNull().isEqualTo(userId);
        }

        @Test
        @DisplayName("예외(NoSuchElement): 이메일이 일치하는 유저가 없으면")
        void noSuchElement_WhenNoMatchEmail() {
            //given
            String email = "test@test.com";
            String password = "password";
            StubHttpServletRequest request = new StubHttpServletRequest();

            //when
            Exception exception = catchException(() -> loginHandler.login(email, password, request));

            //then
            assertThat(exception).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("예외(NoSuchElement): 비밀번호가 일치하지 않으면")
        void noSuchElement_WhenNoMatchPassword() {
            //given
            String userId = UUID.randomUUID().toString();
            String email = "test@test.com";
            String password = "password";
            User user = User.create(userId, email, password, "test");
            StubHttpServletRequest request = new StubHttpServletRequest();
            String wrongPassword = "wrongPassword";

            userDatabase.save(user);

            //when
            Exception exception = catchException(() -> loginHandler.login(email, wrongPassword, request));

            //then
            assertThat(exception).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("logout 호출 시")
    class LogoutTest {

        private StubHttpServletRequest request;

        @BeforeEach
        void setUp() {
            request = new StubHttpServletRequest();
        }

        @Test
        @DisplayName("메인 화면으로 리다이렉트한다.")
        void redirectToMain() {
            //given
            request.getSession();

            //when
            ResponseEntity response = loginHandler.logout(request);

            //then
            assertThat(response.getLocation()).isEqualTo("/");
        }
    }
}