package codesquad.servlet;

import codesquad.fixture.http.EmptyRequestResponseFixture;
import codesquad.fixture.http.MockRequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

class IndexServletTest implements EmptyRequestResponseFixture {
    private MockRequestDispatcher mockRequestDispatcher;
    private MockArticleQueryDao mockArticleQueryDao;
    private IndexServlet indexServlet;

    @BeforeEach
    void setUp() {
        mockRequestDispatcher = new MockRequestDispatcher();
        mockArticleQueryDao = new MockArticleQueryDao();
        indexServlet = new IndexServlet(mockArticleQueryDao);
    }

    @Nested
    @DisplayName("GET /")
    class IndexServletIs {
        @Test
        @DisplayName("forward to : /WEB-INF/views/index.jsp")
        void doGet() throws ServletException, IOException {
            // given
            HttpServletRequest request = emptyRequest(mockRequestDispatcher);
            HttpServletResponse response = emptyResponse();

            // when
            indexServlet.doGet(request, response);

            // then
            Assertions.assertEquals(mockRequestDispatcher.getPath(), "/WEB-INF/views/index.jsp");
            Assertions.assertTrue(mockRequestDispatcher.isForwarded());
        }
    }
}