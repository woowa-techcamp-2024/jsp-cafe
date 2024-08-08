package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Reply;
import cafe.questions.repository.ReplyRepository;
import cafe.users.User;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class RepliesServlet extends MappingHttpServlet {
    private final ReplyRepository replyRepository;
    private final Gson gson = new Gson();

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

        String requestBody = getRequestBody(req);
        LinkedHashMap<String, Object> parsedReq = gson.fromJson(requestBody, LinkedHashMap.class);
        articleId = ((Double) parsedReq.get("articleId")).longValue();
        content = parsedReq.get("content").toString();

        if (content.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("content is empty");
            return;
        }

        Reply reply = new Reply(articleId, user.getId(), content);
        Reply save = replyRepository.save(reply);
        gson.toJson(save, resp.getWriter());
    }

    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
