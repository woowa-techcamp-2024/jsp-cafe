package codesquad.jspcafe.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.common.MockTemplate;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserLogoutServlet은")
class UserLogoutServletTest extends MockTemplate {

    private final UserLogoutServlet userLogoutServlet = new UserLogoutServlet();

    @Test
    @DisplayName("/users/logout POST 요청을 처리하여 로그아웃을 수행한다.")
    void doPost() throws ServletException, IOException {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        given(request.getSession(any(boolean.class))).willReturn(session);
        // Act
        userLogoutServlet.doPost(request, response);
        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("/index.html");
    }

    @Test
    @DisplayName("/users/logout POST 요청을 처리하여 세션이 없을 경우 리다이렉트한다.")
    void doPostSessionIsNull() throws ServletException, IOException {
        // Arrange
        given(request.getSession(any(boolean.class))).willReturn(null);
        // Act
        userLogoutServlet.doPost(request, response);
        // Assert
        verify(response).sendRedirect("/index.html");
    }
}