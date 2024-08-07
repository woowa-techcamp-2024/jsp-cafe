package codesquad.jspcafe.domain.article.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.common.MockTemplate;
import codesquad.jspcafe.domain.article.payload.request.ArticleUpdateRequest;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.service.ArticleService;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("ArticleServlet은")
class ArticleServletTest extends MockTemplate {

    @InjectMocks
    private ArticleServlet articleServlet;

    @Mock
    private ArticleService articleService;

    @Test
    @DisplayName("서블릿을 초기화하여 서블릿 컨텍스트에서 ArticleService와 ReplyService를 가져온다.")
    void init() throws ServletException {
        // Arrange
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        given(config.getServletContext()).willReturn(context);
        given(context.getAttribute("articleService")).willReturn(articleService);
        // Act
        articleServlet.init(config);
        // Assert
        assertThat(articleServlet)
            .extracting("articleService")
            .isEqualTo(articleService);
    }

    @Nested
    @DisplayName("GET 요청을 처리할 때")
    class describeGet {

        private final Long expectedArticleId = 1L;
        private final ArticleCommonResponse articleCommonResponse = mock(
            ArticleCommonResponse.class);
        private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        @ParameterizedTest
        @MethodSource("exceptionValues")
        @DisplayName("PathInfo가 null이거나 blank인 경우 예외를 반환한다.")
        void throwExceptionWhenPathInfoIsNullOrBlank(String pathInfo) {
            // Arrange
            given(request.getPathInfo()).willReturn(pathInfo);
            // Act & Assert
            assertThatThrownBy(() -> articleServlet.doGet(request, response))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("아티클을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("/questions/{articleId}에 대해 질문을 페이지로 포워딩한다.")
        void forwardToQuestionPage() throws ServletException, IOException {
            // Arrange
            given(request.getPathInfo()).willReturn("/" + expectedArticleId);
            given(articleService.getArticleById(String.valueOf(expectedArticleId))).willReturn(
                articleCommonResponse);
            given(request.getRequestDispatcher("/WEB-INF/jsp/question.jsp")).willReturn(
                requestDispatcher);
            // Act
            articleServlet.doGet(request, response);
            // Assert
            verify(request).setAttribute("article", articleCommonResponse);
            verify(requestDispatcher).forward(request, response);
        }

        @Nested
        @DisplayName("/questions/{articleId}/form에 대해")
        class forFormRequest {

            private final HttpSession session = mock(HttpSession.class);
            private final UserSessionResponse userSessionResponse = mock(UserSessionResponse.class);

            @BeforeEach
            void init() {
                given(request.getPathInfo()).willReturn("/" + expectedArticleId + "/form");
                given(articleService.getArticleById(String.valueOf(expectedArticleId))).willReturn(
                    articleCommonResponse);

                given(request.getSession()).willReturn(session);
                given(session.getAttribute("user")).willReturn(userSessionResponse);
            }

            @Test
            @DisplayName("질문 수정 페이지로 포워딩한다.")
            void forwardToUpdateForm() throws ServletException, IOException {
                // Arrange
                String expectedUserId = "expectedUserId";
                given(articleCommonResponse.getWriterUserId()).willReturn(expectedUserId);
                given(userSessionResponse.getUserId()).willReturn(expectedUserId);
                given(
                    request.getRequestDispatcher("/WEB-INF/jsp/questionUpdateForm.jsp")).willReturn(
                    requestDispatcher);
                // Act
                articleServlet.doGet(request, response);
                // Assert
                verify(request).setAttribute("article", articleCommonResponse);
                verify(requestDispatcher).forward(request, response);
            }

            @Test
            @DisplayName("작성자가 아닌 경우 접근 권한이 없다는 예외를 반환한다.")
            void throwExceptionWhenNotWriter() {
                // Arrange
                String expectedUserId = "expectedUserId";
                given(articleCommonResponse.getWriterUserId()).willReturn("writer");
                given(userSessionResponse.getUserId()).willReturn(expectedUserId);
                // Act & Assert
                assertThatThrownBy(() -> articleServlet.doGet(request, response))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("접근 권한이 없습니다.");
            }

        }

        private static Stream<Arguments> exceptionValues() {
            return Stream.of(
                Arguments.of((String) null),
                Arguments.of("")
            );
        }

    }

    @Nested
    @DisplayName("PUT 요청을 처리할 때")
    class describePut {

        private final HttpSession httpSession = mock(HttpSession.class);
        private final UserSessionResponse userSessionResponse = mock(UserSessionResponse.class);
        private final BufferedReader bufferedReader = mock(BufferedReader.class);


        @Test
        @DisplayName("/questions/{articleId}에 대해 질문을 수정한다.")
        void doPut() throws ServletException, IOException {
            // Arrange
            Long expectedArticleId = 1L;
            given(request.getPathInfo()).willReturn("/" + expectedArticleId);
            given(request.getSession()).willReturn(httpSession);
            given(request.getReader()).willReturn(bufferedReader);
            given(bufferedReader.readLine()).willReturn(
                "id=1&title=title&contents=contents", (String) null);
            given(httpSession.getAttribute("user")).willReturn(userSessionResponse);
            given(userSessionResponse.getId()).willReturn(1L);
            // Act
            articleServlet.doPut(request, response);
            // Assert
            verify(articleService).updateArticle(any(ArticleUpdateRequest.class));
            verify(response).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Nested
    @DisplayName("DELETE 요청을 처리할 때")
    class whenDelete {

        private final HttpSession httpSession = mock(HttpSession.class);
        private final UserSessionResponse userSessionResponse = mock(UserSessionResponse.class);

        @Test
        @DisplayName("/questions/{articleId}에 대해 질문을 삭제한다.")
        void doDelete() throws ServletException, IOException {
            // Arrange
            Long expectedArticleId = 1L;
            given(request.getPathInfo()).willReturn("/" + expectedArticleId);
            given(request.getSession()).willReturn(httpSession);
            given(httpSession.getAttribute("user")).willReturn(userSessionResponse);
            given(userSessionResponse.getId()).willReturn(1L);
            // Act
            articleServlet.doDelete(request, response);
            // Assert
            verify(articleService).deleteArticle(expectedArticleId, 1L);
            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

}