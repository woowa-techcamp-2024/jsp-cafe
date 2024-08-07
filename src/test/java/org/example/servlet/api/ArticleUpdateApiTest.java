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
import java.util.Arrays;
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

public class ArticleUpdateApiTest {

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
    private ArticleUpdateApi servlet;
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
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    public void testDoPutSuccess() throws Exception {
        // Given
        Long articleId = 1L;
        User user = new User(1L, "test@test.com", "nickname", "password", LocalDateTime.now());
        Article article = new Article(articleId, "Old Title", "Old Content", "nickname",
                LocalDateTime.now(), AliveStatus.ALIVE, user.getUserId());

        when(request.getPathInfo()).thenReturn("/1");
        when(session.getAttribute(SessionName.USER.getName())).thenReturn(user);
        when(articleDataHandler.findByArticleId(articleId)).thenReturn(article);
        when(articleDataHandler.update(any(Article.class))).thenReturn(article);

        String jsonInput = "{\"title\":\"New Title\",\"content\":\"New Content\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        // When
        servlet.doPut(request, response);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(articleDataHandler).update(any(Article.class));
        assert (stringWriter.toString().contains("글이 성공적으로 수정되었습니다"));
    }

    @Test
    public void testDoDeleteSuccess() throws Exception {
        // Given
        Long articleId = 1L;
        User user = new User(1L, "test@test.com", "nickname", "password", LocalDateTime.now());
        Article article = new Article(articleId, "Old Title", "Old Content", "nickname",
                LocalDateTime.now(), AliveStatus.ALIVE, user.getUserId());
        Reply reply = new Reply(1L, articleId, "nickname", "Comment 1", AliveStatus.ALIVE, LocalDateTime.now());

        when(request.getPathInfo()).thenReturn("/1");
        when(session.getAttribute(SessionName.USER.getName())).thenReturn(user);
        when(articleDataHandler.findByArticleId(articleId)).thenReturn(article);
        when(replyDataHandler.findAllByArticleId(articleId)).thenReturn(Arrays.asList(reply));
        when(articleDataHandler.update(any(Article.class))).thenReturn(article);

        // When
        servlet.doDelete(request, response);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(replyDataHandler).deleteAllByArticleId(articleId);
        verify(articleDataHandler).update(any(Article.class));
        assert (stringWriter.toString().contains("글이 성공적으로 삭제되었습니다"));
    }

    @Test
    public void testDoDeleteFailureWithOtherUserReply() throws Exception {
        // Given
        Long articleId = 1L;
        User user = new User(1L, "test@test.com", "nickname", "password", LocalDateTime.now());
        User otherUser = new User(2L, "test2@test.com", "nickname2", "password", LocalDateTime.now());

        Article article = new Article(articleId, "Old Title", "Old Content", "nickname",
                LocalDateTime.now(), AliveStatus.ALIVE, user.getUserId());
        Reply reply = new Reply(2L, articleId, "nickname", "Comment 1", AliveStatus.ALIVE, LocalDateTime.now());

        when(request.getPathInfo()).thenReturn("/1");
        when(session.getAttribute(SessionName.USER.getName())).thenReturn(user);
        when(articleDataHandler.findByArticleId(articleId)).thenReturn(article);
        when(replyDataHandler.findAllByArticleId(articleId)).thenReturn(Arrays.asList(reply));

        // When
        servlet.doDelete(request, response);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assert (stringWriter.toString().contains("내가 쓰지 않은 댓글이 있어 글을 삭제하지 못합니다"));
    }
}
