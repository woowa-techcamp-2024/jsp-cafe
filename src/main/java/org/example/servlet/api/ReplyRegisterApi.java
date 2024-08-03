package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import org.example.constance.AliveStatus;
import org.example.constance.DataHandler;
import org.example.constance.SessionName;
import org.example.data.ArticleDataHandler;
import org.example.data.ReplyDataHandler;
import org.example.domain.Article;
import org.example.domain.Reply;
import org.example.domain.User;
import org.example.util.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/api/replies")
public class ReplyRegisterApi extends HttpServlet {
    private final Logger log = (Logger) LoggerFactory.getLogger(ReplyRegisterApi.class);
    private ArticleDataHandler articleDataHandler;
    private ReplyDataHandler replyDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDataHandler = (ArticleDataHandler) config.getServletContext()
                .getAttribute(DataHandler.ARTICLE.getValue());
        replyDataHandler = (ReplyDataHandler) config.getServletContext()
                .getAttribute(DataHandler.REPLY.getValue());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[ReplyAi] doPost called");
        req.setCharacterEncoding("UTF-8");
        // JSON 요청 본문 읽기
        BufferedReader reader = req.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }

        // JSON 파싱
        Map<String, Object> jsonObject = JsonParser.parse(jsonBody.toString());
        Long articleId = (Long) jsonObject.get("articleId");
        String comment = (String) jsonObject.get("comment");
        //
        Article article = articleDataHandler.findByArticleId(articleId);
        //
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute(SessionName.USER.getName());
        //
        Reply reply = new Reply(user.getUserId(), article.getArticleId(), user.getNickname(), comment,
                AliveStatus.ALIVE, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        Reply saveReply = replyDataHandler.insert(reply);
        if (saveReply != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"댓글이 성공적으로 작성 되었습니다.\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"댓글 등록 중 오류가 발생했습니다.\"}");
        }

    }

}
