package lass9436.question.servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lass9436.comment.model.Comment;
import lass9436.comment.model.CommentRepositoryImpl;
import lass9436.question.model.Question;
import lass9436.question.model.QuestionRepositoryImpl;
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
class QuestionServletTest {

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
    private QuestionServlet questionServlet;

    private UserRepositoryImpl userRepository;
    private QuestionRepositoryImpl questionRepository;
    private CommentRepositoryImpl commentRepository;

    @BeforeEach
    void setUp() throws Exception {
        userRepository = new UserRepositoryImpl();
        questionRepository = new QuestionRepositoryImpl();
        commentRepository = new CommentRepositoryImpl();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userRepository")).thenReturn(userRepository);
        when(servletContext.getAttribute("questionRepository")).thenReturn(questionRepository);
        when(servletContext.getAttribute("commentRepository")).thenReturn(commentRepository);

        questionServlet.init(servletConfig);

        // Sample data setup
        User user = new User("testUser", "password", "testUser", "testEmail");
        userRepository.save(user);
    }

    @Test
    @DisplayName("doPost 성공 테스트")
    void testDoPostSuccessful() throws ServletException, IOException {
        // Mock request parameters
        when(request.getParameter("writer")).thenReturn("testUser");
        when(request.getParameter("title")).thenReturn("testTitle");
        when(request.getParameter("contents")).thenReturn("testContents");

        // Mock session
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("testUser");

        // Call the servlet method
        questionServlet.doPost(request, response);

        // Verify question is saved and redirect is called
        List<Question> questions = questionRepository.findAll();
        assertEquals(1, questions.size());
        Question question = questions.get(0);
        assertEquals("testUser", question.getWriter());
        assertEquals("testTitle", question.getTitle());
        assertEquals("testContents", question.getContents());

        verify(response).sendRedirect("/");
    }

    @Test
    @DisplayName("doPost 실패 테스트 - 사용자 권한 없음")
    void testDoPostUserNotAuthorized() throws ServletException, IOException {
        // Mock request parameters
        when(request.getParameter("writer")).thenReturn("testUser");
        when(request.getParameter("title")).thenReturn("testTitle");
        when(request.getParameter("contents")).thenReturn("testContents");

        // Mock session
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("otherUser");

        // Call the servlet method
        questionServlet.doPost(request, response);

        // Verify no question is saved and error response
        List<Question> questions = questionRepository.findAll();
        assertTrue(questions.isEmpty());

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
    }

    @Test
    @DisplayName("doPut 성공 테스트")
    void testDoPutSuccessful() throws ServletException, IOException {
        // Mock session
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("testUser");

        // Mock request JSON data
        String jsonString = "{\"seq\":1,\"writer\":\"testUser\",\"title\":\"testTitle\",\"contents\":\"testContents\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonString));
        when(request.getReader()).thenReturn(reader);

        // Add a question to the repository
        Question existingQuestion = new Question(1L, 1L, "testUser", "oldTitle", "oldContents");
        questionRepository.save(existingQuestion);

        // Call the servlet method
        questionServlet.doPut(request, response);

        // Verify question is updated and response status is OK
        Question updatedQuestion = questionRepository.findByQuestionSeq(1L);
        assertNotNull(updatedQuestion);
        assertEquals("testTitle", updatedQuestion.getTitle());
        assertEquals("testContents", updatedQuestion.getContents());

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("doPut 실패 테스트 - 사용자 권한 없음")
    void testDoPutUserNotAuthorized() throws ServletException, IOException {
        // Mock session
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("otherUser");

        // Mock request JSON data
        String jsonString = "{\"seq\":1,\"writer\":\"testUser\",\"title\":\"testTitle\",\"contents\":\"testContents\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonString));
        when(request.getReader()).thenReturn(reader);

        // Add a question to the repository
        Question existingQuestion = new Question(1L, 1L, "testUser", "oldTitle", "oldContents");
        questionRepository.save(existingQuestion);

        // Call the servlet method
        questionServlet.doPut(request, response);

        // Verify question is not updated and error response
        Question updatedQuestion = questionRepository.findByQuestionSeq(1L);
        assertNotNull(updatedQuestion);
        assertEquals("oldTitle", updatedQuestion.getTitle());
        assertEquals("oldContents", updatedQuestion.getContents());

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
    }

    @Test
    @DisplayName("doDelete 성공 테스트")
    void testDoDeleteSuccessful() throws ServletException, IOException {
        // Mock session
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userSeq")).thenReturn(1L);

        // Add a question and a comment to the repository
        Question question = new Question(1L, 1L, "testUser", "testTitle", "testContents");
        questionRepository.save(question);
        Comment comment = new Comment(1L, 1L, "testUser", "testComment");
        commentRepository.save(comment);

        // Mock request JSON data
        String jsonString = "{\"seq\":1}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonString));
        when(request.getReader()).thenReturn(reader);

        // Call the servlet method
        questionServlet.doDelete(request, response);

        // Verify question and comments are deleted and response status is OK
        assertNull(questionRepository.findByQuestionSeq(1L));
        assertTrue(commentRepository.findByQuestionSeq(1L).isEmpty());

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("doDelete 실패 테스트 - 사용자 권한 없음")
    void testDoDeleteUserNotAuthorized() throws ServletException, IOException {
        // Mock session
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userSeq")).thenReturn(2L);

        // Add a question and a comment to the repository
        Question question = new Question(1L, 1L, "testUser", "testTitle", "testContents");
        questionRepository.save(question);
        Comment comment = new Comment(1L, 1L, "testUser", "testComment");
        commentRepository.save(comment);

        // Mock request JSON data
        String jsonString = "{\"seq\":1}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonString));
        when(request.getReader()).thenReturn(reader);

        // Call the servlet method
        questionServlet.doDelete(request, response);

        // Verify question and comments are not deleted and error response
        assertNotNull(questionRepository.findByQuestionSeq(1L));
        assertFalse(commentRepository.findByQuestionSeq(1L).isEmpty());

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
    }

    @Test
    @DisplayName("doDelete 실패 테스트 - 다른 사용자가 작성한 댓글 있음")
    void testDoDeleteOtherUserCommentExists() throws ServletException, IOException {
        // Mock session
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userSeq")).thenReturn(1L);

        // Add a question and comments to the repository
        Question question = new Question(1L, 1L, "testUser", "testTitle", "testContents");
        questionRepository.save(question);
        Comment comment1 = new Comment(1L, 1L, "testUser", "testComment");
        Comment comment2 = new Comment(2L, 1L, "testUser2", "otherUserComment");
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        // Mock request JSON data
        String jsonString = "{\"seq\":1}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonString));
        when(request.getReader()).thenReturn(reader);

        // Call the servlet method
        questionServlet.doDelete(request, response);

        // Verify question and comments are not deleted and error response
        assertNotNull(questionRepository.findByQuestionSeq(1L));
        assertFalse(commentRepository.findByQuestionSeq(1L).isEmpty());

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
    }
}
