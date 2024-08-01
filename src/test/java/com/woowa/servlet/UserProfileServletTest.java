package com.woowa.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.handler.UserHandler;
import com.woowa.model.User;
import com.woowa.support.StubHttpServletRequest;
import com.woowa.support.StubHttpServletResponse;
import com.woowa.support.UserFixture;
import jakarta.servlet.ServletException;
import java.io.IOException;
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
            request.setRequestUri("/users/" + user.getUserId() + "/edit");
            request.setAttribute("nickname", "updateNickname");

            userDatabase.save(user);

            //when
            userProfileServlet.doPost(request, response);

            //then
            assertThat(response.getRedirectLocation()).isEqualTo("/users/" + user.getUserId());
        }
    }

    @Nested
    @DisplayName("doGet 호출 시")
    class DoGetTest {

        @Test
        @DisplayName("개인 정보 수정 폼으로 이동한다.")
        void dispatchToUpdateUserForm() throws ServletException, IOException {
            //given
            User user = UserFixture.user();

            request.setRequestUri("/users/" + user.getUserId() + "/edit");
            userDatabase.save(user);

            //when
            userProfileServlet.doGet(request, response);

            //then
            assertThat(request.getRequestDispatcher().getPath())
                    .isEqualTo("/user/update.jsp");
        }
    }
}