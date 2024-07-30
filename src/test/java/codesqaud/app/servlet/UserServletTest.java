package codesqaud.app.servlet;

import codesqaud.app.dao.user.UserDao;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.User;
import codesqaud.mock.*;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static codesqaud.util.LoginUtils.getLoginUser;
import static codesqaud.util.LoginUtils.login;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserServletTest {
    private UserServlet userServlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletConfig config;
    private UserDao userDao;

    private User user = new User("testUser", "password", "Test User", "test@example.com");


    @BeforeEach
    void setUp() throws ServletException {
        userServlet = new UserServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        config = new MockServletConfig();
        userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        userServlet.init(config);
    }

    /**
     * POST /users
     */
    @Nested
    @DisplayName("회원가입 할 때")
    class SignUpTest {
        private String requestURI = "/users";

        @Test
        @DisplayName("아이디가 중복되지 않으면 회원가입에 성공한다.")
        void when_signUp_then_success() throws ServletException, IOException {
            request.setRequestURI(requestURI);
            request.setParameter("userId", "testUser");
            request.setParameter("password", "password");
            request.setParameter("name", "Test Name");
            request.setParameter("email", "test@example.com");

            userServlet.doPost(request, response);

            User savedUser = userDao.findByUserId("testUser").orElse(null);
            assertThat(savedUser).isNotNull();
            assertThat(response.getRedirectedUrl()).isEqualTo("/users");
        }

        @Test
        @DisplayName("아이디가 중복되면 예외가 발생한다.")
        void given_duplicatedUserId_when_signUp_then_throwException() throws ServletException, IOException {
            userDao.save(user);
            request.setRequestURI(requestURI);
            request.setParameter("userId", "testUser");
            request.setParameter("password", "password");
            request.setParameter("name", "Test Name");
            request.setParameter("email", "test@example.com");

            assertThatThrownBy(() -> userServlet.doPost(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }

    /**
     * GET /users
     */
    @Nested
    @DisplayName("사용자 목록을 조회할 때")
    class UserListTest {
        @Test
        @DisplayName("저장된 사용자를 모두 조회할 수 있다.")
        void given_saveUser_when_getUserList_then_foundUserList() throws ServletException, IOException {
            //given
            request.setRequestURI("/users");
            userDao.save(new User("user1", "pass1", "User 1", "user1@example.com"));
            userDao.save(new User("user2", "pass2", "User 2", "user2@example.com"));

            //when
            userServlet.doGet(request, response);

            //then
            List<User> users = (List<User>) request.getAttribute("users");
            assertThat(users).isNotNull();
            assertThat(users.size()).isEqualTo(2);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/user/list.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("사용자 프로필을 조회할 때")
    class UserProfileTest {
        @Test
        @DisplayName("존재하지 않는 사용자 프로필을 조회하려 하면 NOT FOUND 예외가 발생한다.")
        void given_notExistingUser_when_getUserProfile_then_throwException() {
            request.setRequestURI("/users/profile/999");

            assertThatThrownBy(() -> userServlet.doGet(request, response))
                    .isInstanceOf(HttpException.class)
                    .extracting("statusCode")
                    .isEqualTo(SC_NOT_FOUND);
        }

        @Test
        @DisplayName("존재하는 사용자 프로필을 조회하면 사용자 정보를 획득할 수 있다.")
        void given_existingUser_when_getUserProfile_then_foundUserDetails() throws ServletException, IOException {
            userDao.save(user);
            User savedUser = userDao.findByUserId(user.getUserId()).orElse(null);
            request.setRequestURI("/users/profile/" + savedUser.getId());

            userServlet.doGet(request, response);

            User profileUser = (User) request.getAttribute("user");
            assertThat(profileUser).isNotNull();
            assertThat(profileUser.getId()).isEqualTo(savedUser.getId());

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/user/profile.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("프로필 수정 폼을 조회할 때")
    class ProfileFormTest {
        @Test
        @DisplayName("프로필 수정 폼으로 포워딩된다.")
        void given() throws ServletException, IOException {
            login(config, request);
            request.setRequestURI("/users/profile");

            userServlet.doGet(request, response);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/user/profile_form.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("로그인 폼 조회 시")
    class LoginFormTest {
        @Test
        void 로그인_폼_조회_성공() throws ServletException, IOException {
            request.setRequestURI("/users/login");

            userServlet.doGet(request, response);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/user/login.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    class SignUpFormTest {
        @Test
        void 회원가입_폼_조회_성공() throws ServletException, IOException {
            request.setRequestURI("/users/form");

            userServlet.doGet(request, response);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/user/form.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("프로필 수정을 할 때")
    class ProfileUpdateTest {
        @Test
        @DisplayName("비밀번호 인증을 먼저 하지 않으면 사용자 정보를 수정할 수 없다")
        void given_tryChaneEmailAndPasswordWithoutPasswordChecking_then_cannotUpdate() throws ServletException, IOException {
            //when
            login(config, request);
            request.setParameter("name", "Updated Name");
            request.setParameter("email", "updated@example.com");
            request.setRequestURI("/users/profile");

            //when
            userServlet.doPost(request, response);

            //then
            User updatedUser = userDao.findByUserId(getLoginUser().getUserId()).orElse(null);
            assertThat(updatedUser.getName()).isEqualTo("Test User");
        }

        @Test
        @DisplayName("비밀번호 인증에 성공하면 session에 checkPassword attribute가 생성된다.")
        void given_correctPassword_when_processPasswordChecking_then_setCheckPasswordAttributeInSession() throws ServletException, IOException {
            //given
            login(config, request);
            request.setParameter("password", getLoginUser().getPassword());
            request.setRequestURI("/users/profile");

            //when
            userServlet.doPost(request, response);

            //then
            boolean checkPassword = (Boolean) request.getSession().getAttribute("checkPassword");
            assertThat(checkPassword).isTrue();
        }


        @Test
        @DisplayName("비밀번호 인증 후 수정사항을 입력해서 name과 email을 수정할 수 있다.")
        void given_checkPasswordAndUpdateFields_when_processProfileUpdate_then_updatedUser() throws ServletException, IOException {
            //given
            login(config, request);
            processCheckPassword();
            request.setParameter("name", "Updated Name");
            request.setParameter("email", "updated@example.com");
            request.setRequestURI("/users/profile");

            //when
            userServlet.doPost(request, response);

            //then
            User updatedUser = userDao.findById(getLoginUser().getId()).orElse(null);
            assertThat(updatedUser).isNotNull();
            assertThat(updatedUser.getName()).isEqualTo("Updated Name");
            assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");

            assertThat(response.getRedirectedUrl()).isEqualTo("/users/profile/" + updatedUser.getId());
        }

        private void processCheckPassword() throws ServletException, IOException {
            request.setParameter("password", getLoginUser().getPassword());
            request.setRequestURI("/users/profile");
            userServlet.doPost(request, response);
        }
    }

    /**
     * POST /users/login
     */
    @Nested
    @DisplayName("로그인 할 때")
    class LoginTest {
        @Test
        @DisplayName("가입된 사용자 아이디와 비밀번호를 입력하면 로그인에 성공하고 사용자 세션이 생성된다.")
        void given_registeredUserIdAndPassword_when_login_then_setSession() throws ServletException, IOException {
            //given
            userDao.save(user);

            request.setRequestURI("/users/login");
            request.setParameter("userId", user.getUserId());
            request.setParameter("password", user.getPassword());

            //when
            userServlet.doPost(request, response);

            //then
            User loginUser = (User) request.getSession().getAttribute("loginUser");
            assertThat(loginUser).isNotNull();
            assertThat(loginUser.getUserId()).isEqualTo(user.getUserId());
        }

        @Test
        @DisplayName("가입된 사용자 아이디와 비밀번호 정보를 찾을 수 없으면 예외가 발생한다.")
        void given_incorrectUserIdOrPassword_when_login_then_setSession() throws ServletException, IOException {
            //given
            userDao.save(user);

            request.setRequestURI("/users/login");
            request.setParameter("userId", "incorrectId");
            request.setParameter("password", user.getPassword());

            //when
            userServlet.doPost(request, response);

            //then
            boolean isFailed = (Boolean) request.getAttribute("isFailed");
            assertThat(isFailed).isTrue();
        }
    }

    /**
     * POST /logout
     */
    @Nested
    @DisplayName("로그아웃 할 때")
    class LogoutTest {
        @Test
        @DisplayName("로그아웃되고 세션이 무효화된다.")
        void given_login_when_logout_then_removeSession() throws ServletException, IOException {
            //given
            login(config, request);
            request.setRequestURI("/users/logout");

            //when
            userServlet.doPost(request, response);

            //then
            MockHttpSession session = (MockHttpSession) request.getSession();
            assertThat(session.isInvalidated()).isTrue();
        }
    }
}
