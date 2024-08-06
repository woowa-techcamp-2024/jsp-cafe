package codesquad.jspcafe.domain.article.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.common.MockTemplate;
import codesquad.jspcafe.domain.article.service.ArticleService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("ArticleListServlet은")
class ArticleListServletTest extends MockTemplate {

    @InjectMocks
    private ArticleListServlet articleListServlet;

    @Mock
    private ArticleService articleService;

    @Test
    @DisplayName("서블릿을 초기화하여 서블릿 컨텍스트에서 ArticleService를 가져온다.")
    void init() throws ServletException {
        // Arrange
        ServletContext context = mock(ServletContext.class);
        given(config.getServletContext()).willReturn(context);
        given(context.getAttribute("articleService")).willReturn(articleService);
        // Act
        articleListServlet.init();
        // Assert
        assertThat(articleListServlet)
            .extracting("articleService")
            .isEqualTo(articleService);
    }

    @Nested
    @DisplayName("GET 요청을 처리할 때")
    class describeGet {

        @Mock
        private PrintWriter printWriter;

        @BeforeEach
        void init() throws IOException {
            given(response.getWriter()).willReturn(printWriter);
        }

        @Test
        @DisplayName("Article 갯수를 반환한다.")
        void doGet() throws Exception {
            // Arrange
            given(request.getParameter("page")).willReturn(null);
            given(articleService.getTotalArticlesCount()).willReturn(0L);
            // Act
            articleListServlet.doGet(request, response);
            // Assert
            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(response).setContentType("application/json");
            verify(printWriter).write("0");
        }

        @Test
        @DisplayName("Article 리스트를 반환한다.")
        void doGetWithPage() throws Exception {
            // Arrange
            given(request.getParameter("page")).willReturn("1");
            given(articleService.getArticlesByPage(1)).willReturn(null);
            // Act
            articleListServlet.doGet(request, response);
            // Assert
            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(response).setContentType("application/json");
            verify(printWriter).write(any(String.class));
        }
    }
}