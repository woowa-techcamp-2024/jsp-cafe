package codesquad.global.servlet;

import codesquad.mock.http.MockRequest;
import codesquad.mock.http.MockRequestDispatcher;
import codesquad.mock.http.MockResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

class QnaRegisterFormServletTest {
    private MockRequestDispatcher mockRequestDispatcher;
    private QnaRegisterFormServlet qnaRegisterFormServlet;

    @BeforeEach
    void setUp() {
        mockRequestDispatcher = new MockRequestDispatcher();
        qnaRegisterFormServlet = new QnaRegisterFormServlet();
    }

    @Nested
    @DisplayName("GET /qna/register-form")
    class QnaRegisterFormServletIs {
        @Test
        @DisplayName("forward to : /WEB-INF/views/qna/form.jsp")
        void doGet() throws ServletException, IOException {
            // given
            HttpServletRequest request = new MockRequest("/qna/register-form", "GET", mockRequestDispatcher);
            HttpServletResponse response = new MockResponse();

            // when
            qnaRegisterFormServlet.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/qna/form.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}
