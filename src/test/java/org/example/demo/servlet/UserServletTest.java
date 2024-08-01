package org.example.demo.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.handler.UserHandler;
import org.example.demo.exception.InternalServerError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServletTest {

    private UserServlet userServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private UserHandler userHandler;

    @BeforeEach
    void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        userServlet = spy(new UserServlet());
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userHandler")).thenReturn(userHandler);
        doReturn(servletContext).when(userServlet).getServletContext();
        userServlet.init(servletConfig);
    }

    @Test
    void 루트_경로_요청시_UserHandler의_handleUserList_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        userServlet.service(request, response);

        // Then
        verify(userHandler).handleUserList(eq(request), eq(response), anyList());
    }

    @Test
    void 존재하지_않는_경로_요청시_404_에러가_발생한다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/non-existent");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        userServlet.service(request, response);

        // Then
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 라우팅_중_IOException_발생시_InternalServerError가_throw된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        doThrow(new IOException("Test IO Exception")).when(userHandler).handleUserList(any(), any(), anyList());

        // When & Then
        assertThrows(InternalServerError.class, () -> userServlet.service(request, response));
    }

    @Test
    void GET_요청시_UserHandler의_handleUserProfile_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users/1");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        userServlet.service(request, response);

        // Then
        verify(userHandler).handleUserProfile(eq(request), eq(response), anyList());
    }

    @Test
    void POST_요청시_UserHandler의_handleUserCreate_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        // When
        userServlet.service(request, response);

        // Then
        verify(userHandler).handleUserCreate(eq(request), eq(response), anyList());
    }

    @Test
    void POST_요청시_UserHandler의_handleUserUpdate_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users/1");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        // When
        userServlet.service(request, response);

        // Then
        verify(userHandler).handleUserUpdate(eq(request), eq(response), anyList());
    }

    @Test
    void POST_요청시_UserHandler의_handleUserLogin_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users/login");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        // When
        userServlet.service(request, response);

        // Then
        verify(userHandler).handleUserLogin(eq(request), eq(response), anyList());
    }

    @Test
    void POST_요청시_UserHandler의_handleUserLogout_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users/logout");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        // When
        userServlet.service(request, response);

        // Then
        verify(userHandler).handleUserLogout(eq(request), eq(response), anyList());
    }

    @Test
    void GET_요청시_UserHandler의_handleUserLoginPage_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users/login");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        userServlet.service(request, response);

        // Then
        verify(userHandler).handleUserLoginPage(eq(request), eq(response), anyList());
    }

    @Test
    void GET_요청시_UserHandler의_handleUserFormPage_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/users/form");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        userServlet.service(request, response);

        // Then
        verify(userHandler).handleUserFormPage(eq(request), eq(response), anyList());
    }
}
