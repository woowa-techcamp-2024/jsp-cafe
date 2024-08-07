package codesquad.jspcafe.domain.reply.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.common.MockTemplate;
import codesquad.jspcafe.common.payload.response.CursorPaginationResult;
import codesquad.jspcafe.domain.reply.payload.request.ReplyCreateRequest;
import codesquad.jspcafe.domain.reply.payload.respose.ReplyCommonResponse;
import codesquad.jspcafe.domain.reply.service.ReplyService;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
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

@DisplayName("ReplyServlet는")
class ReplyServletTest extends MockTemplate {

    @InjectMocks
    private ReplyServlet replyServlet;

    @Mock
    private ReplyService replyService;

    private final HttpSession httpSession = mock(HttpSession.class);
    private final UserSessionResponse userSessionResponse = mock(UserSessionResponse.class);

    @Test
    @DisplayName("서블릿을 초기화하여 서블릿 컨텍스트에서 ReplyService를 가져온다.")
    void init() throws ServletException {
        // Arrange
        ServletContext context = mock(ServletContext.class);
        given(config.getServletContext()).willReturn(context);
        given(context.getAttribute("replyService")).willReturn(replyService);
        // Act
        replyServlet.init(config);
        // Assert
        assertThat(replyServlet)
            .extracting("replyService")
            .isEqualTo(replyService);
    }

    @Nested
    @DisplayName("GET 요청을 처리할 때")
    class describeGet {

        private final PrintWriter printWriter = mock(PrintWriter.class);
        private final CursorPaginationResult<ReplyCommonResponse> cursorPaginationResult = mock(
            CursorPaginationResult.class);

        @BeforeEach
        void init() throws IOException {
            given(request.getPathInfo()).willReturn("/1");
            given(response.getWriter()).willReturn(printWriter);
        }

        @DisplayName("/replies로 GET 요청을 보낼 때 댓글을 조회한 후 JSON 바디를 반환한다.")
        @Test
        void doGet() throws Exception {
            // Arrange
            given(replyService.getRepliesByArticleId(any(Long.class))).willReturn(
                cursorPaginationResult);
            // Act
            replyServlet.doGet(request, response);
            // Assert
            verify(response).setContentType("application/json");
            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(printWriter).write(any(String.class));
        }

        @DisplayName("/replies/{replyId}로 GET 요청을 보낼 때 댓글을 조회한 후 JSON 바디를 반환한다.")
        @Test
        void doGetWithReplyId() throws Exception {
            // Arrange
            given(request.getParameter("id")).willReturn("1");
            given(replyService.getRepliesByArticleId(any(Long.class), any(Long.class))).willReturn(
                cursorPaginationResult);
            // Act
            replyServlet.doGet(request, response);
            // Assert
            verify(response).setContentType("application/json");
            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(printWriter).write(any(String.class));
        }

    }

    @Nested
    @DisplayName("POST 요청을 처리할 때")
    class describePost {

        private final PrintWriter printWriter = mock(PrintWriter.class);
        private final ReplyCommonResponse replyCommonResponse = mock(ReplyCommonResponse.class);
        private final Map<String, String[]> parameterMap = Map.of(
            "article", new String[]{"1"},
            "contents", new String[]{"contents"}
        );

        @Test
        @DisplayName("/replies로 들어온 요청을 통해 새로운 댓글을 생성한 후 CREATED 응답과 함께 JSON 바디를 반환한다.")
        void doPost() throws Exception {
            // Arrange

            given(request.getSession()).willReturn(httpSession);
            given(httpSession.getAttribute("user")).willReturn(userSessionResponse);
            given(userSessionResponse.getId()).willReturn(1L);
            given(request.getParameterMap()).willReturn(parameterMap);
            given(replyService.createReply(any(ReplyCreateRequest.class))).willReturn(
                replyCommonResponse);
            given(replyCommonResponse.toString()).willReturn("replyCommonResponse");
            given(response.getWriter()).willReturn(printWriter);
            // Act
            replyServlet.doPost(request, response);
            // Assert
            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            verify(response).setContentType("application/json");
        }

    }

    @Nested
    @DisplayName("DELETE 요청을 처리할 때")
    class describeDelete {

        @ParameterizedTest
        @MethodSource("exceptionValues")
        @DisplayName("/replies/{replyId}의 replyId가 잘못된 경우 NoSuchElementException을 던진다.")
        void doDeleteThrowNoSuchElementException(String pathInfo) {
            // Arrange
            given(request.getPathInfo()).willReturn(pathInfo);
            // Act & Assert
            assertThatThrownBy(() -> replyServlet.doDelete(request, response))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("아티클을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("/replies/{replyId}로 DELETE 요청을 보낼 때 댓글을 삭제한다.")
        void doDelete() throws ServletException, IOException {
            // Arrange
            Long expectedReplyId = 1L;
            String expectedUserId = "test";
            given(request.getPathInfo()).willReturn("/" + expectedReplyId);
            given(request.getSession()).willReturn(httpSession);
            given(httpSession.getAttribute("user")).willReturn(userSessionResponse);
            given(userSessionResponse.getUserId()).willReturn(expectedUserId);
            // Act
            replyServlet.doDelete(request, response);
            // Assert
            verify(replyService).deleteReply(expectedReplyId, expectedUserId);

        }


        private static Stream<Arguments> exceptionValues() {
            return Stream.of(
                Arguments.of((String) null),
                Arguments.of("")
            );
        }
    }
}

