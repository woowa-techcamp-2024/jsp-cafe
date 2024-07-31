package codesquad.jspcafe.domain.article.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.domain.article.service.ArticleService;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ArticleCreateServlet는")
class ArticleCreateServletTest {

    @InjectMocks
    private ArticleCreateServlet articleCreateServlet;

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
        articleCreateServlet.init();
        // Assert
        assertThat(articleCreateServlet)
            .extracting("articleService")
            .isEqualTo(articleService);
    }

    @Test
    @DisplayName("GET 요청을 처리하여 questionForm.jsp 페이지로 포워딩한다.")
    void doGet() throws ServletException, IOException {
        // Arrange
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        given(request.getRequestDispatcher(any(String.class))).willReturn(requestDispatcher);
        // Act
        articleCreateServlet.doGet(request, response);
        // Assert
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("POST 요청을 처리하여 새로운 아티클을 생성한 후 / 페이지로 리디렉션한다.")
    void doPost() throws ServletException, IOException {
        // Arrange
        HttpSession httpSession = mock(HttpSession.class);
        UserSessionResponse sessionResponse = mock(UserSessionResponse.class);
        String userId = "userId";
        Map<String, String[]> map = Map.of();
        given(request.getSession()).willReturn(httpSession);
        given(httpSession.getAttribute("user")).willReturn(sessionResponse);
        given(request.getParameterMap()).willReturn(map);
        given(sessionResponse.getUserId()).willReturn(userId);
        // Act
        articleCreateServlet.doPost(request, response);
        // Assert
        verify(articleService).createArticle(map, userId);
        verify(response).sendRedirect("/");
    }
}