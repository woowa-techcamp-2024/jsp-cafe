package codesquad.global.servlet;

import codesquad.article.handler.IndexHandler;
import codesquad.mock.http.MockRequest;
import codesquad.mock.http.MockRequestDispatcher;
import codesquad.mock.http.MockResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

class IndexServletTest {
    private MockRequestDispatcher mockRequestDispatcher;
    private MockArticleQuery mockArticleQueryDao;
    private IndexHandler indexServlet;

    @BeforeEach
    void setUp() {
        mockRequestDispatcher = new MockRequestDispatcher();
        mockArticleQueryDao = new MockArticleQuery();
        indexServlet = new IndexHandler(mockArticleQueryDao);
    }

    @Nested
    @DisplayName("GET /")
    class IndexServletIs {
        @Test
        @DisplayName("forward to : /WEB-INF/views/index.jsp")
        void doGet() throws ServletException, IOException {
            // given
            HttpServletRequest request = new MockRequest("/", "GET", mockRequestDispatcher);
            HttpServletResponse response = new MockResponse();

            // when
//            indexServlet.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/index.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}
