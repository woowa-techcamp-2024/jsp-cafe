package codesquad.jspcafe.domain.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.common.MockTemplate;
import codesquad.jspcafe.domain.user.payload.request.UserUpdateRequest;
import codesquad.jspcafe.domain.user.payload.response.UserCommonResponse;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("UserInfoServlet은")
class UserInfoServletTest extends MockTemplate {

    @InjectMocks
    private UserInfoServlet userInfoServlet;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("서블릿을 초기화하여 서블릿 컨텍스트에서 UserSerivce를 가져온다.")
    void init() throws ServletException {
        // Arrange
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        given(config.getServletContext()).willReturn(context);
        given(context.getAttribute("userService")).willReturn(userService);
        // Act
        userInfoServlet.init(config);
        // Assert
        assertThat(userInfoServlet)
            .extracting("userService")
            .isEqualTo(userService);
    }

    @Nested
    @DisplayName("GET 요청을 처리하여")
    class describeGet {

        private final UserCommonResponse userCommonResponse = mock(UserCommonResponse.class);
        private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        private final String userId = "userId";

        @MethodSource("exceptionProvider")
        @ParameterizedTest
        @DisplayName("PathInfo가 잘못된 경우 예외를 던진다.")
        void verifyPathInfo(String pathInfo) {
            // Arrange
            given(request.getPathInfo()).willReturn(pathInfo);
            // Act & Assert
            assertThatThrownBy(() -> userInfoServlet.doGet(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 요청입니다.");
        }

        @Test
        @DisplayName("PathInfo가 /form으로 끝나는 경우 사용자 정보 갱신 페이지로 포워딩한다.")
        void doGetForUpdateForm() throws ServletException, IOException {
            // Arrange
            given(userService.getUserById(any(String.class))).willReturn(userCommonResponse);
            given(request.getRequestDispatcher(any(String.class))).willReturn(dispatcher);
            given(request.getPathInfo()).willReturn("/" + userId + "/form");
            given(userCommonResponse.getUserId()).willReturn(userId);
            // Act
            userInfoServlet.doGet(request, response);
            // Assert
            verify(request).setAttribute("userId", userId);
            verify(dispatcher).forward(request, response);
        }

        @Test
        @DisplayName("PathInfo가 /form으로 끝나지 않는 경우 사용자 정보 페이지로 포워딩한다.")
        void doGet() throws ServletException, IOException {
            // Arrange
            given(userService.getUserById(any(String.class))).willReturn(userCommonResponse);
            given(request.getRequestDispatcher(any(String.class))).willReturn(dispatcher);
            given(request.getPathInfo()).willReturn("/" + userId);
            given(userCommonResponse.getUserId()).willReturn(userId);
            // Act
            userInfoServlet.doGet(request, response);
            // Assert
            verify(request).setAttribute("user", userCommonResponse);
            verify(dispatcher).forward(request, response);
        }

        private static Stream<Arguments> exceptionProvider() {
            return Stream.of(
                Arguments.of((String) null),
                Arguments.of("")
            );
        }
    }

    @Nested
    @DisplayName("POST 요청을 처리하여")
    class describePost {

        private final String userId = "userId";
        private final HttpSession httpSession = mock(HttpSession.class);
        private final UserSessionResponse sessionResponse = mock(UserSessionResponse.class);

        @Test
        @DisplayName("PathInfo가 /form으로 끝나지 않는 경우 예외를 던진다.")
        void doPostThrowsException() {
            // Arrange
            given(request.getPathInfo()).willReturn("/" + userId);
            // Act & Assert
            assertThatThrownBy(() -> userInfoServlet.doPost(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 요청입니다.");
        }

        @Test
        @DisplayName("/users에 대해 사용자 자신의 요청이 아닌 경우 예외를 던진다.")
        void doPostThrowsExceptionForAccessDenied() {
            // Arrange
            Map<String, String[]> parameterMap = Map.of(
                "userId", new String[]{userId + 1}
            );
            given(request.getPathInfo()).willReturn("/" + userId + "/form");
            given(request.getSession()).willReturn(httpSession);
            given(request.getParameterMap()).willReturn(parameterMap);
            given(httpSession.getAttribute("user")).willReturn(sessionResponse);
            given(sessionResponse.getUserId()).willReturn(userId);
            // Act & Assert
            assertThatThrownBy(() -> userInfoServlet.doPost(request, response))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("본인의 요청만 수정할 수 있습니다.");
        }

        @Test
        @DisplayName("/users에 대해 사용자 자신의 요청인 경우 사용자 정보를 갱신하고 /users/{userId} 페이지로 리디렉션한다.")
        void doPost() throws ServletException, IOException {
            // Arrange
            Map<String, String[]> parameterMap = Map.of(
                "userId", new String[]{userId},
                "password", new String[]{"password"},
                "username", new String[]{"username"},
                "email", new String[]{"email@gmail.com"}
            );
            given(request.getPathInfo()).willReturn("/" + userId + "/form");
            given(request.getParameterMap()).willReturn(parameterMap);
            given(request.getSession()).willReturn(httpSession);
            given(httpSession.getAttribute("user")).willReturn(sessionResponse);
            given(sessionResponse.getUserId()).willReturn(userId);
            // Act
            userInfoServlet.doPost(request, response);
            // Assert
            verify(userService).updateUserInfo(any(UserUpdateRequest.class));
            verify(response).sendRedirect("/users/" + userId);
        }
    }
}