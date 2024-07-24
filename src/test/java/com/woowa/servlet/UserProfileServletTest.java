package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.handler.UserHandler;
import com.woowa.model.User;
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

class UserProfileServletTest {

    private UserProfileServlet userProfileServlet;
    private UserHandler userHandler;
    private UserDatabase userDatabase;

    private StubHttpServletRequest request;
    private StubHttpServletResponse response;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        userHandler = new UserHandler(userDatabase);
        userProfileServlet = new UserProfileServlet(userHandler, userDatabase);

        request = new StubHttpServletRequest();
        response = new StubHttpServletResponse();
    }

    @Nested
    @DisplayName("doPost 호출 시")
    class DoPostTest {

        @Test
        @DisplayName("회원 프로필로 리다이렉트한다.")
        void redirectToProfile() throws ServletException, IOException {
            //given
            User user = UserFixture.user();
            request.setRequestUri("/users/" + user.getUserId());
            request.setAttribute("nickname", "updateNickname");

            userDatabase.save(user);

            //when
            userProfileServlet.doPost(request, response);

            //then
            assertThat(response.getRedirectLocation()).isEqualTo("/users/" + user.getUserId());
        }
    }
}