package codesquad.jspcafe.domain.article.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import codesquad.jspcafe.common.MockTemplate;
import codesquad.jspcafe.domain.article.service.ArticleService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
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

}