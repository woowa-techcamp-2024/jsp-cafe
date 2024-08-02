package lass9436.user.servlet;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.user.model.User;
import lass9436.user.model.UserRepository;

import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserPageServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPageServlet userPageServlet;

    @BeforeEach
    void setUp() throws Exception {
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userRepository")).thenReturn(userRepository);
        userPageServlet.init(servletConfig);
    }

    @Test
    @DisplayName("GET /userPage?action=register 성공 테스트")
    void testHandleRegister() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("register");
        when(request.getRequestDispatcher("/WEB-INF/user/register.jsp")).thenReturn(requestDispatcher);

        userPageServlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /userPage?action=login 성공 테스트")
    void testHandleLogin() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("login");
        when(request.getRequestDispatcher("/WEB-INF/user/login.jsp")).thenReturn(requestDispatcher);

        userPageServlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /userPage?action=login-failed 성공 테스트")
    void testHandleLoginFailed() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("login-failed");
        when(request.getRequestDispatcher("/WEB-INF/user/loginFailed.jsp")).thenReturn(requestDispatcher);

        userPageServlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /userPage?action=list 성공 테스트")
    void testHandleList() throws ServletException, IOException {
        List<User> users = List.of(new User("user1", "password1", "name1", "email1"));
        when(request.getParameter("action")).thenReturn("list");
        when(userRepository.findAll()).thenReturn(users);
        when(request.getRequestDispatcher("/WEB-INF/user/list.jsp")).thenReturn(requestDispatcher);

        userPageServlet.doGet(request, response);

        verify(request).setAttribute("users", users);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /userPage?action=detail 성공 테스트")
    void testHandleDetail() throws ServletException, IOException {
        User user = new User("user1", "password1", "name1", "email1");
        when(request.getParameter("action")).thenReturn("detail");
        when(request.getParameter("seq")).thenReturn("1");
        when(userRepository.findByUserSeq(1L)).thenReturn(user);
        when(request.getRequestDispatcher("/WEB-INF/user/detail.jsp")).thenReturn(requestDispatcher);

        userPageServlet.doGet(request, response);

        verify(request).setAttribute("user", user);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /userPage?action=update 성공 테스트")
    void testHandleUpdate() throws ServletException, IOException {
        User user = new User("user1", "password1", "name1", "email1");
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("seq")).thenReturn("1");
        when(userRepository.findByUserSeq(1L)).thenReturn(user);
        when(request.getRequestDispatcher("/WEB-INF/user/update.jsp")).thenReturn(requestDispatcher);

        userPageServlet.doGet(request, response);

        verify(request).setAttribute("user", user);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /userPage?action=unknown 예외 테스트")
    void testUnknownAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("unknown");

        userPageServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "unknown action: unknown");
    }

    @Test
    @DisplayName("GET /userPage?action=null 예외 테스트")
    void testNullAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);

        userPageServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "action is null or empty");
    }
}
