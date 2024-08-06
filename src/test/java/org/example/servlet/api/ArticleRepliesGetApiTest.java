package org.example.servlet.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.example.constance.AliveStatus;
import org.example.constance.DataHandler;
import org.example.data.ReplyDataHandler;
import org.example.domain.Reply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ArticleRepliesGetApiTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ReplyDataHandler replyDataHandler;

    private ArticleRepliesGetApi servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        servlet = new ArticleRepliesGetApi();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(DataHandler.REPLY.getValue())).thenReturn(replyDataHandler);

        servlet.init(servletConfig);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoGet() throws Exception {
        // Given
        Long articleId = 1L;
        when(request.getPathInfo()).thenReturn("/" + articleId);

        List<Reply> mockReplies = Arrays.asList(
                new Reply(1L, articleId, "user1", "Comment 1", AliveStatus.ALIVE, LocalDateTime.now()),
                new Reply(2L, articleId, "user2", "Comment 2", AliveStatus.ALIVE, LocalDateTime.now())
        );
        when(replyDataHandler.findAllByArticleId(articleId)).thenReturn(mockReplies);

        // When
        servlet.doGet(request, response);

        // Then
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        writer.flush();
        String result = stringWriter.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<Reply> resultReplies = objectMapper.readValue(result,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Reply.class));

        assertEquals(mockReplies.size(), resultReplies.size());
        for (int i = 0; i < mockReplies.size(); i++) {
            assertEquals(mockReplies.get(i).getReplyId(), resultReplies.get(i).getReplyId());
            assertEquals(mockReplies.get(i).getArticleId(), resultReplies.get(i).getArticleId());
            assertEquals(mockReplies.get(i).getAuthor(), resultReplies.get(i).getAuthor());
            assertEquals(mockReplies.get(i).getComment(), resultReplies.get(i).getComment());
        }
    }
}