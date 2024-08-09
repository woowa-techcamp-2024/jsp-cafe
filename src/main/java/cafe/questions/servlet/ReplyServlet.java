package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Reply;
import cafe.questions.repository.ReplyRepository;
import cafe.users.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ReplyServlet extends MappingHttpServlet {
    private final ReplyRepository replyRepository;
    private final Gson gson = new Gson();

    @Override
    public List<String> mappings() {
        return List.of("/replies/*");
    }

    public ReplyServlet(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long articleId = Long.valueOf(req.getPathInfo().substring(1));
        String cursorStr = req.getParameter("cursor");
        Long cursor = cursorStr != null ? Long.valueOf(cursorStr) : 0L;
        int limit = Optional.ofNullable(req.getParameter("limit"))
                        .map(Integer::parseInt)
                        .filter(l -> l > 0)
                        .orElse(0);
        List<Reply> replies = replyRepository.findByArticleId(articleId, cursor, limit + 1);

        boolean hasMore = replies.size() > limit;
        if (hasMore) {
            replies = replies.subList(0, limit);
        }

        Long nextCursor = hasMore ? replies.get(replies.size() - 1).getId() : null;

        JsonObject response = new JsonObject();
        response.add("comments", gson.toJsonTree(replies));
        response.addProperty("nextCursor", nextCursor != null ? nextCursor.toString() : null);
        response.addProperty("hasMore", hasMore);

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        Reply reply = replyRepository.findById(id);

        if (reply == null || !reply.getUserId().equals(user.getId())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("only the author can delete the reply");
            return;
        }

        replyRepository.deleteById(id);
    }
}
