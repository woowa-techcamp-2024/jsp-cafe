package com.woowa.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.*;

import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.User;
import com.woowa.support.StubHttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LoginHandlerTest {

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
}