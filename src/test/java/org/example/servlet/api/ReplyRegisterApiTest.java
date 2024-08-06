package org.example.servlet.api;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.example.constance.AliveStatus;
import org.example.constance.DataHandler;
import org.example.constance.SessionName;
import org.example.data.ArticleDataHandler;
import org.example.data.ReplyDataHandler;
import org.example.domain.Article;
import org.example.domain.Reply;
import org.example.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReplyRegisterApiTest {

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
    private ArticleDataHandler articleDataHandler;
    @Mock
    private ReplyDataHandler replyDataHandler;

    @InjectMocks
    private ReplyRegisterApi servlet;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(DataHandler.ARTICLE.getValue())).thenReturn(articleDataHandler);
        when(servletContext.getAttribute(DataHandler.REPLY.getValue())).thenReturn(replyDataHandler);

        servlet.init(servletConfig);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    public void testDoPostSuccess() throws Exception {
        // Given
        Long articleId = 1L;
        String comment = "This is a test comment";
        User user = new User(1L, "test@test.com", "nickname", "password", LocalDateTime.now());
        Article article = new Article(articleId, "Test Title", "Test Content", "nickname",
                LocalDateTime.now(), AliveStatus.ALIVE, user.getUserId());

        String jsonInput = "{\"articleId\":" + articleId + ",\"comment\":\"" + comment + "\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(session.getAttribute(SessionName.USER.getName())).thenReturn(user);
        when(articleDataHandler.findByArticleId(articleId)).thenReturn(article);
        Reply reply = new Reply(user.getUserId(), articleId, user.getNickname(), comment, AliveStatus.ALIVE,
                LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        when(replyDataHandler.insert(any(Reply.class))).thenReturn(reply);
        // When
        servlet.doPost(request, response);
        // Then
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(replyDataHandler).insert(any(Reply.class));
        assert (stringWriter.toString().contains("댓글이 성공적으로 작성 되었습니다"));
    }

    @Test
    public void testDoPostFailure() throws Exception {
        // Given
        Long articleId = 1L;
        String comment = "This is a test comment";
        User user = new User(1L, "test@test.com", "nickname", "password", LocalDateTime.now());
        Article article = new Article(articleId, "Test Title", "Test Content", "nickname",
                LocalDateTime.now(), AliveStatus.ALIVE, user.getUserId());

        String jsonInput = "{\"articleId\":" + articleId + ",\"comment\":\"" + comment + "\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(session.getAttribute(SessionName.USER.getName())).thenReturn(user);
        when(articleDataHandler.findByArticleId(articleId)).thenReturn(article);
        when(replyDataHandler.insert(any(Reply.class))).thenReturn(null);

        // When
        servlet.doPost(request, response);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(replyDataHandler).insert(any(Reply.class));
        assert (stringWriter.toString().contains("댓글 등록 중 오류가 발생했습니다"));
    }
}