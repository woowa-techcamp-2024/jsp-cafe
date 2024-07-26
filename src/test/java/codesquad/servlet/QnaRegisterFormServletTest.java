package codesquad.servlet;

import codesquad.fixture.http.EmptyRequestResponseFixture;
import codesquad.fixture.http.MockRequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

class QnaRegisterFormServletTest implements EmptyRequestResponseFixture {
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
            HttpServletRequest request = emptyRequest(mockRequestDispatcher);
            HttpServletResponse response = emptyResponse();

            // when
            qnaRegisterFormServlet.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/qna/form.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }

}