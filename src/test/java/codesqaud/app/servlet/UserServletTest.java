package codesqaud.app.servlet;

import codesqaud.app.dao.UserDao;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.User;
import codesqaud.mock.MockHttpServletRequest;
import codesqaud.mock.MockHttpServletResponse;
import codesqaud.mock.MockRequestDispatcher;
import codesqaud.mock.MockServletConfig;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

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

    @Nested
    class SignUpTest {
        @Test
        void 회원가입_성공() throws ServletException, IOException {
            request.setRequestURI("/users");
            request.setParameter("userId", "testUser");
            request.setParameter("password", "password");
            request.setParameter("name", "Test Name");
            request.setParameter("email", "test@example.com");

            userServlet.doPost(request, response);

            User savedUser = userDao.findByUserId("testUser").orElse(null);
            assertThat(savedUser).isNotNull();
            assertThat(response.getRedirectedUrl()).isEqualTo("/users");
        }
    }

    @Nested
    class 사용자목록_조회_테스트 {

        @Test
        void 사용자목록_조회() throws ServletException, IOException {
            request.setRequestURI("/users");
            userDao.save(new User("user1", "pass1", "User 1", "user1@example.com"));
            userDao.save(new User("user2", "pass2", "User 2", "user2@example.com"));

            userServlet.doGet(request, response);

            List<User> users = (List<User>) request.getAttribute("users");
            assertThat(users).isNotNull();
            assertThat(users.size()).isEqualTo(2);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/user/list.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    class UserProfileTest {
        @Test
        void 존재하지_않는_사용자_프로필_조회() {
            request.setRequestURI("/users/999");

            assertThatThrownBy(() -> userServlet.doGet(request, response))
                    .isInstanceOf(HttpException.class)
                    .hasMessage("해당 아이디를 가진 사용자는 찾을 수 없습니다.");
        }

        @Test
        void 존재하는_사용자_프로필_조회() throws ServletException, IOException {
            userDao.save(user);
            User savedUser = userDao.findByUserId(user.getUserId()).orElse(null);
            request.setRequestURI("/users/" + savedUser.getId());

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
    class ProfileFormTest {
        @Test
        void 프로필_수정_폼_조회_성공() throws ServletException, IOException {
            userDao.save(user);
            User savedUser = userDao.findByUserId(user.getUserId()).orElse(null);
            request.setRequestURI("/users/profile/" + savedUser.getId());

            userServlet.doGet(request, response);

            User profileUser = (User) request.getAttribute("user");
            assertThat(profileUser).isNotNull();
            assertThat(profileUser.getId()).isEqualTo(savedUser.getId());

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/user/profile_form.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }

        @Test
        void 존재하지_않는_사용자_프로필_수정_폼_조회() {
            request.setRequestURI("/users/profile/999");

            assertThatThrownBy(() -> userServlet.doGet(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }

    @Nested
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
    class ProfileUpdateTest {

        @Test
        void 프로필_수정_성공() throws ServletException, IOException {
            userDao.save(user);
            User savedUser = userDao.findByUserId(user.getUserId()).orElse(null);
            request.setRequestURI("/users/" + savedUser.getId());
            request.setParameter("name", "Updated Name");
            request.setParameter("email", "updated@example.com");
            request.setParameter("password", "newpassword");

            userServlet.doPost(request, response);

            User updatedUser = userDao.findById(savedUser.getId()).orElse(null);
            assertThat(updatedUser).isNotNull();
            assert updatedUser != null;
            assertThat(updatedUser.getName()).isEqualTo("Updated Name");
            assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
            assertThat(updatedUser.getPassword()).isEqualTo("newpassword");

            assertThat(response.getRedirectedUrl()).isEqualTo("/users/" + savedUser.getId());
        }

        @Test
        void 존재하지_않는_사용자_프로필_수정() throws IOException {
            request.setRequestURI("/users/999");
            request.setParameter("name", "Updated Name");
            request.setParameter("email", "updated@example.com");
            request.setParameter("password", "newpassword");

            assertThatThrownBy(() -> userServlet.doPost(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }
}
