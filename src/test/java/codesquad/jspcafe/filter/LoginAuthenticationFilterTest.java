package codesquad.jspcafe.filter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginAuthenticationFilter는")
class LoginAuthenticationFilterTest {

    private final LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void init() {
        given(request.getSession()).willReturn(httpSession);
    }

    @Test
    @DisplayName("로그인한 사용자가 접근 불가능한 URI에 접근하는 경우 메인 페이지로 리다이렉트한다.")
    void doFilterWhenUserIsLoggedIn() throws IOException, ServletException {
        // Arrange
        given(httpSession.getAttribute("user")).willReturn(new Object());
        // Act
        loginAuthenticationFilter.doFilter(request, response, filterChain);
        // Assert
        verify(response).sendRedirect("/");
    }

    @Test
    @DisplayName("로그인하지 않은 사용자가 접근 불가능한 URI에 접근하는 경우 다음 필터로 요청을 전달한다.")
    void doFilterWhenUserIsNotLoggedIn() throws IOException, ServletException {
        // Arrange
        given(httpSession.getAttribute("user")).willReturn(null);
        // Act
        loginAuthenticationFilter.doFilter(request, response, filterChain);
        // Assert
        verify(filterChain).doFilter(request, response);
    }
}