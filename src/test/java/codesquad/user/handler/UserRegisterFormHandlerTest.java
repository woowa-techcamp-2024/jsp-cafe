package codesquad.user.handler;

import codesquad.mock.http.MockRequest;
import codesquad.mock.http.MockRequestDispatcher;
import codesquad.mock.http.MockResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

class UserRegisterFormHandlerTest {
    private MockRequestDispatcher mockRequestDispatcher;
    private UserRegisterFormHandler userRegisterFormHandler;

    @BeforeEach
    void setUp() {
        mockRequestDispatcher = new MockRequestDispatcher();
        userRegisterFormHandler = new UserRegisterFormHandler();
    }

    @Nested
    @DisplayName("GET /users/register-form")
    class UserRegisterFormHandlerIs {
        @Test
        @DisplayName("forward to : /WEB-INF/views/user/form.jsp")
        void doGet() throws ServletException, IOException {
            // given
            HttpServletRequest request = new MockRequest("/users/register-form", "GET", mockRequestDispatcher);
            HttpServletResponse response = new MockResponse();

            // when
            userRegisterFormHandler.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/user/form.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}
