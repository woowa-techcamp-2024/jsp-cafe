package lass9436.comment.servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lass9436.comment.model.Comment;
import lass9436.comment.model.CommentRepositoryImpl;
import lass9436.user.model.User;
import lass9436.user.model.UserRepositoryImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServletTest {

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

    @InjectMocks
    private CommentServlet commentServlet;

    private CommentRepositoryImpl commentRepository;
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() throws Exception {
        commentRepository = new CommentRepositoryImpl();
        userRepository = new UserRepositoryImpl();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("commentRepository")).thenReturn(commentRepository);

        commentServlet.init(servletConfig);

        // Sample data setup
        User user = new User("testUser", "password", "testUser", "testEmail");
        userRepository.save(user);

        // Sample data setup
        Comment comment = new Comment(1L, 1L, "testUser", "testContents");
        commentRepository.save(comment);
    }

    @Test
    @DisplayName("POST 성공 테스트")
    void testDoPostSuccessful() throws ServletException, IOException {
        // sample data delete
        commentRepository.deleteByCommentSeq(1L);

        // Mock request parameters
        when(request.getParameter("contents")).thenReturn("testContents");
        when(request.getParameter("questionSeq")).thenReturn("1");

        // Mock session
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userName")).thenReturn("testUser");
        when(session.getAttribute("userSeq")).thenReturn(1L);

        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Call the servlet method
        commentServlet.doPost(request, response);

        // Verify comment is saved
        Comment savedComment = commentRepository.findAll().get(0);
        assertNotNull(savedComment);
        assertEquals("testContents", savedComment.getContents());
        assertEquals("testUser", savedComment.getWriter());
        assertEquals(1L, savedComment.getUserSeq());
        assertEquals(1L, savedComment.getQuestionSeq());

        // Verify response
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(savedComment);
        assertEquals(expectedJson, stringWriter.toString());
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("DELETE 성공 테스트")
    void testDoDeleteSuccessful() throws ServletException, IOException {
        // Mock request parameters
        when(request.getParameter("seq")).thenReturn("1");

        // Mock session
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userSeq")).thenReturn(1L);

        // Call the servlet method
        commentServlet.doDelete(request, response);

        // Verify comment is deleted
        assertTrue(commentRepository.findAll().isEmpty());

        // Verify response status
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("DELETE 실패 테스트 - 댓글 없음")
    void testDoDeleteCommentNotFound() throws ServletException, IOException {
        // Mock request parameters
        when(request.getParameter("seq")).thenReturn("2");

        // Call the servlet method
        commentServlet.doDelete(request, response);

        // Verify response status
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE 실패 테스트 - 세션 없음")
    void testDoDeleteNoSession() throws ServletException, IOException {
        // Mock request parameters
        when(request.getParameter("seq")).thenReturn("1");

        // Mock no session
        when(request.getSession(false)).thenReturn(null);

        // Call the servlet method
        commentServlet.doDelete(request, response);

        // Verify response status
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("DELETE 실패 테스트 - 사용자 권한 없음")
    void testDoDeleteUserNotAuthorized() throws ServletException, IOException {
        // Mock request parameters
        when(request.getParameter("seq")).thenReturn("1");

        // Mock session
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userSeq")).thenReturn(2L);

        // Call the servlet method
        commentServlet.doDelete(request, response);

        // Verify response status
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
