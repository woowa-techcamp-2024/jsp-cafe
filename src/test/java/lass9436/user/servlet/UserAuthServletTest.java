package lass9436.user.servlet;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lass9436.user.model.User;
import lass9436.user.model.UserRepository;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class UserAuthServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private ServletContext servletContext;

    @Mock
    private HttpSession session;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAuthServlet userAuthServlet;

    @BeforeEach
    void setUp() throws Exception {
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userRepository")).thenReturn(userRepository);
        userAuthServlet.init(servletConfig);
    }

    @Test
    @DisplayName("doPost 성공 테스트")
    void testDoPostSuccessful() throws IOException {
        // Mock request parameters
        when(request.getParameter("userId")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("testPassword");

        // Mock session
        when(request.getSession()).thenReturn(session);

        // Mock user repository
        User user = new User("testUser", "testPassword", "testName", "testEmail");
        when(userRepository.findByUserId("testUser")).thenReturn(user);

        // Call the servlet method
        userAuthServlet.doPost(request, response);

        // Verify session attributes are set
        verify(session).setAttribute("userId", "testUser");
        verify(session).setAttribute("userSeq", user.getUserSeq());
        verify(session).setAttribute("userName", "testName");

        // Verify redirect
        verify(response).sendRedirect("/");
    }

    @Test
    @DisplayName("doPost 실패 테스트 - 잘못된 비밀번호")
    void testDoPostWrongPassword() throws IOException {
        // Mock request parameters
        when(request.getParameter("userId")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("wrongPassword");

        // Mock user repository
        User user = new User("testUser", "testPassword", "testName", "testEmail");
        when(userRepository.findByUserId("testUser")).thenReturn(user);

        // Call the servlet method
        userAuthServlet.doPost(request, response);

        // Verify error response
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Wrong password");
    }

    @Test
    @DisplayName("doPost 실패 테스트 - 사용자 없음")
    void testDoPostUserNotFound() throws IOException {
        // Mock request parameters
        when(request.getParameter("userId")).thenReturn("nonExistingUser");
        when(request.getParameter("password")).thenReturn("testPassword");

        // Mock user repository
        when(userRepository.findByUserId("nonExistingUser")).thenReturn(null);

        // Call the servlet method
        userAuthServlet.doPost(request, response);

        // Verify error response
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "User not found");
    }

    @Test
    @DisplayName("doDelete 성공 테스트")
    void testDoDeleteSuccessful() throws IOException {
        // Mock session
        when(request.getSession()).thenReturn(session);

        // Call the servlet method
        userAuthServlet.doDelete(request, response);

        // Verify session is invalidated
        verify(session).invalidate();

        // Verify response status
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
