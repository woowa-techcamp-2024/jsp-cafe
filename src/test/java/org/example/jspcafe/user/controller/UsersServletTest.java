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
                User.builder()
                        .id(1L)
                        .userId("user1")
                        .email("user1@example.com")
                        .nickname("Nick1")
                        .build(),
                User.builder()
                        .id(2L)
                        .userId("user2")
                        .email("user1@example.com")
                        .nickname("Nick1")
                        .build());

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
        User expectedUser = User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .userId(userId)
                .build();
        verify(userService).register(expectedUser);
        verify(response).sendRedirect("users");
    }
}