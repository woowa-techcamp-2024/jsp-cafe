package codesquad.jspcafe.domain.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.common.MockTemplate;
import codesquad.jspcafe.domain.user.payload.request.UserLoginRequest;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("UserLoginServlet은")
class UserLoginServletTest extends MockTemplate {

    @InjectMocks
    private UserLoginServlet userLoginServlet;

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
        userLoginServlet.init(config);
        // Assert
        assertThat(userLoginServlet)
            .extracting("userService")
            .isEqualTo(userService);
    }

    @Test
    @DisplayName("/users/login GET 요청을 처리하여 로그인 페이지로 포워딩한다.")
    void doGet() throws ServletException, IOException {
        // Arrange
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        given(request.getRequestDispatcher("/WEB-INF/jsp/userLogin.jsp")).willReturn(dispatcher);
        // Act
        userLoginServlet.doGet(request, response);
        // Assert
        verify(dispatcher).forward(request, response);
    }

    @Nested
    @DisplayName("/users/login POST 요청을 처리하여")
    class describePost {

        private final Map<String, String[]> parameterMap = Map.of(
            "userId", new String[]{"test"},
            "password", new String[]{"test"}
        );

        @Test
        @DisplayName("로그인을 수행한다.")
        void doPost() throws ServletException, IOException {
            // Arrange
            UserSessionResponse sessionResponse = mock(UserSessionResponse.class);
            HttpSession session = mock(HttpSession.class);
            given(request.getParameterMap()).willReturn(parameterMap);
            given(userService.loginUser(any(UserLoginRequest.class))).willReturn(sessionResponse);
            given(request.getSession(any(boolean.class))).willReturn(session);
            // Act
            userLoginServlet.doPost(request, response);
            // Assert
            verify(session).setAttribute("user", sessionResponse);
            verify(response).sendRedirect("/index.html");
        }

        @ParameterizedTest
        @MethodSource("exceptionProvider")
        @DisplayName("사용자가 존재하지 않는 경우 로그인 실패 페이지로 포워딩한다.")
        void doPostFailed(Exception exception) throws ServletException, IOException {
            // Arrange
            given(request.getParameterMap()).willReturn(parameterMap);
            given(userService.loginUser(any(UserLoginRequest.class))).willThrow(exception);
            RequestDispatcher dispatcher = mock(RequestDispatcher.class);
            given(request.getRequestDispatcher("/WEB-INF/jsp/userLoginFailed.jsp")).willReturn(
                dispatcher);
            // Act
            userLoginServlet.doPost(request, response);
            // Assert
            verify(request).setAttribute("loginException", exception.getMessage());
            verify(dispatcher).forward(request, response);
        }

        private static Stream<Arguments> exceptionProvider() {
            return Stream.of(
                Arguments.of(new NoSuchElementException("사용자가 존재하지 않습니다.")),
                Arguments.of(new SecurityException("비밀번호가 잘못되었습니다."))
            );
        }
    }
}