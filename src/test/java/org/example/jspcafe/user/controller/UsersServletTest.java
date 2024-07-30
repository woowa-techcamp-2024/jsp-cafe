package org.example.jspcafe.user.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.user.User;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class UsersServletTest {

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
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private UsersServlet usersServlet;

    @BeforeEach
    void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("UserRepository")).thenReturn(userRepository);
        when(servletContext.getAttribute("UserService")).thenReturn(userService);

        usersServlet = new UsersServlet();
        usersServlet.init(servletConfig);
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        // Given
        List<User> users = Arrays.asList(
                new User("user1", "pass1", "Nick1", "user1@example.com"),
                new User("user2", "pass2", "Nick2", "user2@example.com")
        );
        when(userRepository.getAll()).thenReturn(users);
        when(request.getRequestDispatcher("/user/list.jsp")).thenReturn(requestDispatcher);

        // When
        usersServlet.doGet(request, response);

        // Then
        verify(request).setAttribute("users", users);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoPost() throws IOException {
        // Given
        String email = "test@example.com";
        String userId = "testUser";
        String nickname = "TestNick";
        String password = "testPass";

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("userId")).thenReturn(userId);
        when(request.getParameter("nickname")).thenReturn(nickname);
        when(request.getParameter("password")).thenReturn(password);

        // When
        usersServlet.doPost(request, response);

        // Then
        User expectedUser = new User(userId, password, nickname, email);
        verify(userService).register(expectedUser);
        verify(response).sendRedirect("users");
    }
}