package codesquad.servlet;

import codesquad.fixture.http.EmptyRequestResponseFixture;
import codesquad.fixture.http.MockRequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserRegisterFormServletTest implements EmptyRequestResponseFixture {
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
        @DisplayName("forward to : /")
        void doGet() throws ServletException, IOException {
            // given
            HttpServletRequest request = emptyRequest(mockRequestDispatcher);
            HttpServletResponse response = emptyResponse();

            // when
            userRegisterFormServlet.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/user/form.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}