package codesquad.servlet;

import codesquad.fixture.http.EmptyRequestResponseFixture;
import codesquad.fixture.http.MockRequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

class LoginServletTest implements EmptyRequestResponseFixture {
    private MockRequestDispatcher mockRequestDispatcher;
    private MockUserDao mockUserDao;
    private LoginServlet loginServlet;

    @BeforeEach
    void setUp() {
        mockRequestDispatcher = new MockRequestDispatcher();
        mockUserDao = new MockUserDao();
        loginServlet = new LoginServlet(mockUserDao);
    }

    @Nested
    @DisplayName("GET /login")
    class IndexServletIs {
        @Test
        @DisplayName("forward to : /WEB-INF/views/user/login.jsp")
        void doGet() throws ServletException, IOException {
            // given
            HttpServletRequest request = emptyRequest(mockRequestDispatcher);
            HttpServletResponse response = emptyResponse();

            // when
            loginServlet.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/user/login.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}