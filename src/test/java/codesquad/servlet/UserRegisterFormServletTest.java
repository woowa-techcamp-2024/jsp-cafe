package codesquad.servlet;

import codesquad.http.MockRequest;
import codesquad.http.MockRequestDispatcher;
import codesquad.http.MockResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

class UserRegisterFormServletTest {
    private MockRequestDispatcher mockRequestDispatcher;
    private UserRegisterFormServlet userRegisterFormServlet;

    @BeforeEach
    void setUp() {
        mockRequestDispatcher = new MockRequestDispatcher();
        userRegisterFormServlet = new UserRegisterFormServlet();
    }

    @Nested
    @DisplayName("GET /users/register-form")
    class UserRegisterFormServletIs {
        @Test
        @DisplayName("forward to : /WEB-INF/views/user/form.jsp")
        void doGet() throws ServletException, IOException {
            // given
            HttpServletRequest request = new MockRequest("/users/register-form", "GET", mockRequestDispatcher);
            HttpServletResponse response = new MockResponse();

            // when
            userRegisterFormServlet.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/user/form.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}