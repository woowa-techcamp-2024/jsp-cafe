package org.example.jspcafe.user.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.user.User;
import org.example.jspcafe.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

@DisplayName("UserServlet 클래스")
class UserServletTest {
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

    private UserServlet userServlet;

    @BeforeEach
    void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("UserService")).thenReturn(userService);

        userServlet = new UserServlet();
        userServlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doGet 메소드는")
    class doGet {

        @DisplayName("user pk를 path variable 에 넣으면 user 가 반환된다.")
        @Test
        void testDoGet() throws ServletException, IOException {
            // Given
            Long userId = 1L;
            User user = new User(userId, "testUser", "password", "Test User", "test@example.com");
            when(request.getPathInfo()).thenReturn("/1");
            when(userService.findById(userId)).thenReturn(user);
            when(request.getRequestDispatcher("/user/edit.jsp")).thenReturn(requestDispatcher);

            // When
            userServlet.doGet(request, response);

            // Then
            verify(request).setAttribute("user", user);
            verify(requestDispatcher).forward(request, response);
        }

        @DisplayName("user pk 가 path variable 에 없으면 IllegalArgumentException 이 발생한다")
        @Test
        void testDoGetWithMissingPathInfo() throws ServletException, IOException {
            // Given
            when(request.getPathInfo()).thenReturn(null);

            // When/Then
            try {
                userServlet.doGet(request, response);
            } catch (IllegalArgumentException e) {
                verify(userService, never()).findById(anyLong());
                verify(request, never()).getRequestDispatcher(anyString());
            }
        }

        @DisplayName("숫자가 아닌 path variable 이 들어가면 IllegalArgumentException 이 발생한다.")
        @Test
        void testDoGetWithInvalidPathInfo() throws ServletException, IOException {
            // Given
            when(request.getPathInfo()).thenReturn("/invalid");

            // When/Then
            try {
                userServlet.doGet(request, response);
            } catch (IllegalArgumentException e) {
                verify(userService, never()).findById(anyLong());
                verify(request, never()).getRequestDispatcher(anyString());
            }
        }
    }

    @Nested
    @DisplayName("doPut 메소드는")
    class doPut {

        @Test
        @DisplayName("성공적으로 처리하면 유저가 업데이트 되고 /user/{user.id}로 redirect 된다")
        void testDoPut() throws IOException {
            // Given
            Long userId = 1L;
            when(request.getPathInfo()).thenReturn("/1");
            when(request.getParameter("userId")).thenReturn("updatedUser");
            when(request.getParameter("nickname")).thenReturn("Updated User");
            when(request.getParameter("password")).thenReturn("newPassword");
            when(request.getParameter("email")).thenReturn("updated@example.com");

            // When
            userServlet.doPut(request, response);

            // Then
            verify(userService).updateUser(userId, "updatedUser", "Updated User", "newPassword", "updated@example.com");
            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(response).addHeader("Location", "/users/1");
        }
    }
}