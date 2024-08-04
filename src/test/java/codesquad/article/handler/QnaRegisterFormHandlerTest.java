package codesquad.article.handler;

import codesquad.mock.http.MockRequest;
import codesquad.mock.http.MockRequestDispatcher;
import codesquad.mock.http.MockResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

class QnaRegisterFormHandlerTest {
    private MockRequestDispatcher mockRequestDispatcher;
    private QnaRegisterFormHandler qnaRegisterFormHandler;

    @BeforeEach
    void setUp() {
        mockRequestDispatcher = new MockRequestDispatcher();
        qnaRegisterFormHandler = new QnaRegisterFormHandler();
    }

    @Nested
    @DisplayName("GET /qna/register-form")
    class QnaRegisterFormHandlerIs {
        @Test
        @DisplayName("forward to : /WEB-INF/views/qna/form.jsp")
        void doGet() throws ServletException, IOException {
            // given
            HttpServletRequest request = new MockRequest("/qna/register-form", "GET", mockRequestDispatcher);
            HttpServletResponse response = new MockResponse();

            // when
            qnaRegisterFormHandler.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/qna/form.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}
