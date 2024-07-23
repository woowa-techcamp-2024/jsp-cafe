package codesqaud.app.servlet;

import codesqaud.app.dao.UserDao;
import codesqaud.app.model.User;
import codesqaud.mock.MockHttpServletRequest;
import codesqaud.mock.MockHttpServletResponse;
import codesqaud.mock.MockRequestDispatcher;
import codesqaud.mock.MockServletConfig;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("회원가입 할 때")
    class SignUpTest {

        @Test
        @DisplayName("회원가입이 성공하면 DB에 저장되고 Redirect 한다")
        void 회원가입_성공() throws ServletException, IOException {
            // Given
            request.setParameter("userId", "testUser");
            request.setParameter("password", "password");
            request.setParameter("name", "Test Name");
            request.setParameter("email", "test@example.com");

            // When
            userServlet.doPost(request, response);

            // Then
            User savedUser = userDao.findByUserId("testUser").orElse(null);
            assertThat(savedUser).isNotNull();
            assertThat(response.getRedirectedUrl()).isEqualTo("/users");
        }
    }

    @Nested
    @DisplayName("사용자 목록을 조회할 때")
    class 사용자목록_조회_테스트 {

        @Test
        @DisplayName("저장된 사용자 목록을 attribute에 세팅하고 dispatcher를 설정한다")
        void 사용자목록_조회() throws ServletException, IOException {
            // Given
            request.setRequestURI("/users");
            userDao.save(new User("user1", "pass1", "User 1", "user1@example.com"));
            userDao.save(new User("user2", "pass2", "User 2", "user2@example.com"));

            // When
            userServlet.doGet(request, response);

            // Then
            List<User> users = (List<User>) request.getAttribute("users");
            assertThat(users).isNotNull();
            assertThat(users.size()).isEqualTo(2);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/user/list.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("사용자 프로필을 조회할 때")
    class userProfile {

        @Test
        @DisplayName("해당하는 사용자가 없으면 예외가 발생한다")
        void 존재하지_않는_사용자_프로필_조회() {
            // Given
            request.setRequestURI("/users/999");

            // When & Then
            assertThatThrownBy(() -> userServlet.doGet(request, response))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("해당하는 사용자가 있으면 attribute에 등록되고 dispatcher가 설정된다")
        void 존재하는_사용자_프로필_조회() throws ServletException, IOException {
            // Given
            userDao.save(user);
            User savedUser = userDao.findByUserId(user.getUserId()).orElse(null);
            request.setRequestURI("/users/" + savedUser.getId());

            // When
            userServlet.doGet(request, response);

            // Then
            User profileUser = (User) request.getAttribute("userProfile");
            assertThat(profileUser).isNotNull();
            assertThat(profileUser.getId()).isEqualTo(user.getId());

            assertThat(request.getRequestDispatcher().getForwardedPath()).isEqualTo("/user/profile.jsp");
            assertThat(request.getRequestDispatcher().getForwardCount()).isEqualTo(1);
        }
    }
}
