package lass9436.question.servlet;

import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
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
class QuestionPageServletTest {

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
    private RequestDispatcher requestDispatcher;

    @InjectMocks
    private QuestionPageServlet questionPageServlet;

    private QuestionRepositoryImpl questionRepository;
    private CommentRepositoryImpl commentRepository;
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() throws Exception {
        questionRepository = new QuestionRepositoryImpl();
        commentRepository = new CommentRepositoryImpl();
        userRepository = new UserRepositoryImpl();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("questionRepository")).thenReturn(questionRepository);
        when(servletContext.getAttribute("commentRepository")).thenReturn(commentRepository);

        questionPageServlet.init(servletConfig);

        // Sample data setup
        User user = new User("testUser", "password", "testUser", "testEmail");
        userRepository.save(user);
    }

    @Test
    @DisplayName("GET /questionPage?action=register 성공 테스트")
    void testHandleRegister() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("register");
        when(request.getRequestDispatcher("/WEB-INF/question/register.jsp")).thenReturn(requestDispatcher);

        questionPageServlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /questionPage?action=list 성공 테스트")
    void testHandleList() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("list");
        when(request.getRequestDispatcher("/WEB-INF/question/list.jsp")).thenReturn(requestDispatcher);

        questionPageServlet.doGet(request, response);

        verify(request).setAttribute(eq("questions"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /questionPage?action=detail 성공 테스트")
    void testHandleDetail() throws ServletException, IOException {
        Question question = new Question(1L, 1L, "testUser", "testTitle", "testContents");
        questionRepository.save(question);
        Comment comment = new Comment(1L, 1L, "testUser", "testComment");
        commentRepository.save(comment);

        when(request.getParameter("action")).thenReturn("detail");
        when(request.getParameter("seq")).thenReturn("1");
        when(request.getRequestDispatcher("/WEB-INF/question/detail.jsp")).thenReturn(requestDispatcher);

        questionPageServlet.doGet(request, response);

        verify(request).setAttribute("question", question);
        verify(request).setAttribute(eq("comments"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /questionPage?action=update 성공 테스트")
    void testHandleUpdateSuccessful() throws ServletException, IOException {
        Question question = new Question(1L, 1L, "testUser", "testTitle", "testContents");
        questionRepository.save(question);

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("seq")).thenReturn("1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userSeq")).thenReturn(1L);
        when(request.getRequestDispatcher("/WEB-INF/question/update.jsp")).thenReturn(requestDispatcher);

        questionPageServlet.doGet(request, response);

        verify(request).setAttribute("question", question);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("GET /questionPage?action=update 실패 테스트 - 사용자 권한 없음")
    void testHandleUpdateUserNotAuthorized() throws ServletException, IOException {
        Question question = new Question(1L, 1L, "testUser", "testTitle", "testContents");
        questionRepository.save(question);

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("seq")).thenReturn("1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userSeq")).thenReturn(2L);

        questionPageServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "you are not allowed to update this question");
    }

    @Test
    @DisplayName("GET /questionPage?action=invalid 테스트 - 잘못된 액션")
    void testInvalidAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("invalid");

        questionPageServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "unknown action: invalid");
    }

    @Test
    @DisplayName("GET /questionPage?action= 테스트 - 빈 액션")
    void testEmptyAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("");

        questionPageServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "action is null or empty");
    }
}
