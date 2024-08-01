package org.example.demo.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.handler.CommentHandler;
import org.example.demo.handler.PostHandler;
import org.example.demo.exception.InternalServerError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PostServletTest {

    private PostServlet postServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private PostHandler postHandler;

    @Mock
    private CommentHandler commentHandler;

    @BeforeEach
    void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        postServlet = spy(new PostServlet());
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("postHandler")).thenReturn(postHandler);
        when(servletContext.getAttribute("commentHandler")).thenReturn(commentHandler);
        doReturn(servletContext).when(postServlet).getServletContext();
        postServlet.init(servletConfig);
    }

    @Test
    void 루트_경로_요청시_PostHandler의_handleGetPost_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/posts/1");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        postServlet.service(request, response);

        // Then
        verify(postHandler).handleGetPost(eq(request), eq(response), anyList());
    }

    @Test
    void 존재하지_않는_경로_요청시_404_에러가_발생한다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/non-existent");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        postServlet.service(request, response);

        // Then
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 라우팅_중_IOException_발생시_InternalServerError가_throw된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/posts/1");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        doThrow(new IOException("Test IO Exception")).when(postHandler).handleGetPost(any(), any(), anyList());

        // When & Then
        assertThrows(InternalServerError.class, () -> postServlet.service(request, response));
    }

    @Test
    void POST_요청시_PostHandler의_handleCreatePost_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/posts");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        // When
        postServlet.service(request, response);

        // Then
        verify(postHandler).handleCreatePost(eq(request), eq(response), anyList());
    }

    @Test
    void PUT_요청시_PostHandler의_handleUpdatePost_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/posts/1");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("PUT");

        // When
        postServlet.service(request, response);

        // Then
        verify(postHandler).handleUpdatePost(eq(request), eq(response), anyList());
    }

    @Test
    void DELETE_요청시_PostHandler의_handleDeletePost_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/posts/1");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("DELETE");

        // When
        postServlet.service(request, response);

        // Then
        verify(postHandler).handleDeletePost(eq(request), eq(response), anyList());
    }

    @Test
    void POST_요청시_CommentHandler의_createComment_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/posts/1/comments");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        // When
        postServlet.service(request, response);

        // Then
        verify(commentHandler).createComment(eq(request), eq(response), anyList());
    }

    @Test
    void DELETE_요청시_CommentHandler의_deleteComment_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/posts/1/comments/1");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("DELETE");

        // When
        postServlet.service(request, response);

        // Then
        verify(commentHandler).deleteComment(eq(request), eq(response), anyList());
    }

    @Test
    void PUT_요청시_CommentHandler의_updateComment_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/posts/1/comments/1");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("PUT");

        // When
        postServlet.service(request, response);

        // Then
        verify(commentHandler).updateComment(eq(request), eq(response), anyList());
    }
}
