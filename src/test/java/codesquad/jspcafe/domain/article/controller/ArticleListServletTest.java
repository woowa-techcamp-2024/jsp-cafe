package codesquad.jspcafe.domain.article.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.domain.article.payload.response.ArticleContentResponse;
import codesquad.jspcafe.domain.article.service.ArticleService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ArticleListServlet은")
class ArticleListServletTest {

    @InjectMocks
    private ArticleListServlet articleListServlet;

    @Mock
    private ArticleService articleService;

    @Mock
    private ServletConfig config;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;


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

    @Test
    @DisplayName("GET 요청을 처리하여 questionList.jsp 페이지로 포워딩한다.")
    void doGet() throws ServletException, IOException {
        // Arrange
        List<ArticleContentResponse> articleContentResponses = List.of();
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        given(articleService.findAllArticle()).willReturn(articleContentResponses);
        given(request.getRequestDispatcher("/WEB-INF/jsp/questionList.jsp")).willReturn(
            requestDispatcher);
        // Act
        articleListServlet.doGet(request, response);
        // Assert
        verify(request).setAttribute("questionList", articleContentResponses);
        verify(requestDispatcher).forward(request, response);
    }
}