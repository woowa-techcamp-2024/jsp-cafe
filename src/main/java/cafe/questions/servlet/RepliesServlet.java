package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Reply;
import cafe.questions.repository.ReplyRepository;
import cafe.users.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class RepliesServlet extends MappingHttpServlet {
    private final ReplyRepository replyRepository;

    @Override
    public List<String> mappings() {
        return List.of("/replies");
    }

    public RepliesServlet(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }

        Long articleId;
        String content;

        try {
            LinkedHashMap<String, Object> parsedReq = new JSONParser(req.getReader()).parseObject();
            articleId = Long.parseLong(parsedReq.get("articleId").toString());
            content = parsedReq.get("content").toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (content.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("content is empty");
            return;
        }

        Reply reply = new Reply(articleId, user.getId(), content);
        replyRepository.save(reply);
    }
}
