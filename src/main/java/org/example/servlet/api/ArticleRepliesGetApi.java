package org.example.servlet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.example.constance.DataHandler;
import org.example.data.ReplyDataHandler;
import org.example.domain.Reply;

@WebServlet("/api/articles/replies/*")
public class ArticleRepliesGetApi extends HttpServlet {
    private ReplyDataHandler replyDataHandler;
    private ObjectMapper objectMapper;
    private static final int REPLIES_PER_PAGE = 5;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        replyDataHandler = (ReplyDataHandler) config.getServletContext()
                .getAttribute(DataHandler.REPLY.getValue());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Long articleId = Long.valueOf(pathInfo.substring(1));
        List<Reply> replies = replyDataHandler.findAllByArticleId(articleId);
        String jsonReplies = objectMapper.writeValueAsString(replies);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(jsonReplies);
        out.flush();
    }
}
