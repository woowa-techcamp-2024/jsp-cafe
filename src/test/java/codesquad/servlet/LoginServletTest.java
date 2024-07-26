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
    @DisplayName("GET /login 요청은")
    class IndexServletIs {
        @Test
        @DisplayName("/WEB-INF/views/user/login.jsp에 forward한다.")
        void doGet() throws ServletException, IOException {
            HttpServletRequest request = emptyRequest(mockRequestDispatcher);
            HttpServletResponse response = emptyResponse();
            loginServlet.doGet(request, response);

            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/user/login.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}