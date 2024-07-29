package camp.woowa.jspcafe.filter;

import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

class AuthenticateFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private HttpSession session;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticateFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilter_AuthorizedUser() throws IOException, ServletException {
        // Arrange
        User user = new User(1L, "userId", "password", "name", "email");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(userService.validateAuthorization(user)).thenReturn(true);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void testDoFilter_UnauthorizedUser() throws IOException, ServletException {
        // Arrange
        User user = new User(1L, "userId", "password", "name", "email");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(userService.validateAuthorization(user)).thenReturn(false);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/user/login");
        verify(chain, never()).doFilter(request, response);
    }



    @Test
    void testDoFilter_NoSession() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/user/login");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilter_NoUser() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/user/login");
        verify(chain, never()).doFilter(request, response);
    }
}
