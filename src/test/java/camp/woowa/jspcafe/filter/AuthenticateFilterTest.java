package camp.woowa.jspcafe.filter;

import camp.woowa.jspcafe.core.ServiceLocator;
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


    @Test
    void testDoFilter_AuthorizedUser() throws IOException, ServletException {
        // Arrange
        userService = mock(UserService.class);
        when(userService.validateAuthorization(any())).thenReturn(true);
        ServiceLocator.registerService(UserService.class, userService);
        MockitoAnnotations.openMocks(this);

        User user = new User(1L, "userId", "password", "name", "email");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);


        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void testDoFilter_UnauthorizedUser() throws IOException, ServletException {
        // Arrange
        userService = mock(UserService.class);
        when(userService.validateAuthorization(any())).thenReturn(false);
        ServiceLocator.registerService(UserService.class, userService);
        MockitoAnnotations.openMocks(this);

        User user = new User(1L, "userId", "password", "name", "email");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/user/login");
        verify(chain, never()).doFilter(request, response);
    }



    @Test
    void testDoFilter_NoSession() throws IOException, ServletException {
        // Arrange
        MockitoAnnotations.openMocks(this);

        when(request.getSession(false)).thenReturn(null);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/user/login");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilter_NoUser() throws IOException, ServletException {
        // Arrange
        MockitoAnnotations.openMocks(this);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).sendRedirect("/user/login");
        verify(chain, never()).doFilter(request, response);
    }
}
